package com.example.scoped_storage_example.modules.media_store.ui

import android.Manifest
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.arkivanov.decompose.ComponentContext
import com.example.scoped_storage_example.R
import com.example.scoped_storage_example.core.data.ComponentToast
import com.example.scoped_storage_example.core.data.CurrentTime
import com.example.scoped_storage_example.core.data.Logger
import com.example.scoped_storage_example.core.data.PermissionValidator
import com.example.scoped_storage_example.core.ui.widgets.DialogData
import com.example.scoped_storage_example.core.utils.TypeFilter
import com.example.scoped_storage_example.core.utils.componentCoroutineScope
import com.example.scoped_storage_example.modules.media_store.data.MediaStoreGateway
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RealMediaStoreComponent(
    componentContext: ComponentContext,
    private val onOutput: (MediaStoreComponent.Output) -> Unit,
    private val componentToast: ComponentToast,
    private val logger: Logger,
    private val currentTime: CurrentTime,
    private val permissionValidator: PermissionValidator,
    private val mediaStore: MediaStoreGateway
) : ComponentContext by componentContext, MediaStoreComponent {

    private val coroutineScope = componentCoroutineScope()

    override var dialogData: DialogData? by mutableStateOf(null)

    override var filter: TypeFilter by mutableStateOf(TypeFilter.All)

    override var mediaFiles: List<MediaFileViewData>? by mutableStateOf(null)

    override var selectedUri: Uri? by mutableStateOf(null)

    override var selectedMediaFile: DetailedImageFileViewData? by mutableStateOf(null)

    override var isShowImageFileContent: Boolean by mutableStateOf(false)

    init {
        logger.log("Init AppStorageComponent")
        backPressedHandler.register(::onBackPressed)

        onLoadMedia()
    }

    override fun onLoadMedia() {
        permissionValidator.validatePermission(
            permission = Manifest.permission.READ_EXTERNAL_STORAGE,
            messageRes = R.string.media_store_read_permission_request,
            onUpdateDialogData = {
                dialogData = it
            },
            onGranted = {
                coroutineScope.launch {
                    refresh()
                    logger.log("Media loaded")
                }
            },
            onDenied = {
                onOutput(MediaStoreComponent.Output.NavigationRequested)
            }
        )
    }

    override fun onSaveBitmap(bitmap: Bitmap) {
        val action: () -> Unit = {
            coroutineScope.launch {
                val fileName = "photo " + currentTime.currentTimeString
                mediaStore.writeImage(fileName, bitmap)
                logger.log("$fileName saved")
                componentToast.show(R.string.media_store_save_photo_completed)
                refresh()
            }
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            action()
        } else {
            permissionValidator.validatePermission(
                permission = Manifest.permission.WRITE_EXTERNAL_STORAGE,
                messageRes = R.string.media_store_save_photo_permission_request,
                onUpdateDialogData = {
                    dialogData = it
                },
                onGranted = {
                    action()
                },
                onDenied = {
                    componentToast.show(R.string.media_store_save_photo_failed)
                }
            )
        }
    }

    override fun onChangeFilter(filter: TypeFilter) {
        this.filter = filter
        onLoadMedia()
    }

    override fun onFileClick(uri: Uri) {
        selectedUri = uri
        coroutineScope.launch {
            mediaStore.openMediaFile(uri)?.let {
                selectedMediaFile = it.toViewData()
                isShowImageFileContent = true
                logger.log("Image ${it.name} loaded")
            } ?: componentToast.show(R.string.media_store_file_open_fail)
        }
    }

    override fun onFileLongClick(uri: Uri) {
        selectedUri = uri
    }

    override fun onFileRemoveClick(uri: Uri) {
        val action: () -> Unit = {
            coroutineScope.launch {
                if (mediaStore.removeMediaFile(uri)) {
                    refresh()
                    componentToast.show(R.string.media_store_file_remove_completed)
                }
            }
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            action()
        } else {
            permissionValidator.validatePermission(
                permission = Manifest.permission.WRITE_EXTERNAL_STORAGE,
                messageRes = R.string.media_store_remove_file_permission_request,
                onUpdateDialogData = {
                    dialogData = it
                },
                onGranted = {
                    action()
                }
            )
        }
    }

    private suspend fun refresh() {
        withContext(Dispatchers.IO) {
            mediaFiles = mediaStore.loadMediaFiles(filter).map { it.toViewData() }
        }
    }

    private fun onBackPressed(): Boolean {
        return when {
            isShowImageFileContent -> {
                isShowImageFileContent = false
                logger.log("Media file content viewer closed")
                true
            }
            else -> false
        }
    }
}