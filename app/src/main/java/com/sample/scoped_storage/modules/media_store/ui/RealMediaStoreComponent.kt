package com.sample.scoped_storage.modules.media_store.ui

import android.Manifest
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.arkivanov.decompose.ComponentContext
import com.sample.scoped_storage.R
import com.sample.scoped_storage.core.data.ComponentToast
import com.sample.scoped_storage.core.data.CurrentTime
import com.sample.scoped_storage.core.data.Logger
import com.sample.scoped_storage.core.data.PermissionValidator
import com.sample.scoped_storage.core.ui.widgets.DialogData
import com.sample.scoped_storage.core.utils.TypeFilter
import com.sample.scoped_storage.core.utils.componentCoroutineScope
import com.sample.scoped_storage.modules.media_store.data.MediaStoreGateway
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

    override var isRefreshing: Boolean by mutableStateOf(false)

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
        isRefreshing = true
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            // Since Android TIRAMISU (API 33), the permissions to read the media file have been changed
            // Reed more: https://developer.android.com/about/versions/13/behavior-changes-13#granular-media-permissions
            requestTeramisuLoadPermission()
        } else {
            requestLoadPermission()
        }
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

        // WRITE_EXTERNAL_STORAGE permission is not actual since Android Q (API 29)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            action()
        } else {
            permissionValidator.requestPermission(
                permission = Manifest.permission.WRITE_EXTERNAL_STORAGE,
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
                mediaStore.removeMediaFile(uri) {
                    when (it) {
                        MediaStoreGateway.FileRemoveResult.Completed -> {
                            coroutineScope.launch {
                                refresh()
                            }
                            componentToast.show(R.string.media_store_file_remove_completed)
                        }
                        MediaStoreGateway.FileRemoveResult.PermissionDenied -> {
                            componentToast.show(R.string.media_store_file_remove_permission_denied)
                        }
                        MediaStoreGateway.FileRemoveResult.Error -> {
                            componentToast.show(R.string.media_store_file_remove_error)
                        }
                    }
                }
            }
        }

        // WRITE_EXTERNAL_STORAGE permission is not actual since Android Q (API 29)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            action()
        } else {
            permissionValidator.requestPermission(
                permission = Manifest.permission.WRITE_EXTERNAL_STORAGE,
                onUpdateDialogData = {
                    dialogData = it
                },
                onGranted = {
                    action()
                },
                onDenied = {
                    componentToast.show(R.string.media_store_file_remove_permission_denied)
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

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun requestTeramisuLoadPermission() {
        permissionValidator.requestPermission(
            permission = Manifest.permission.READ_MEDIA_IMAGES,
            onUpdateDialogData = {
                dialogData = it
            },
            onGranted = {
                permissionValidator.requestPermission(Manifest.permission.READ_MEDIA_VIDEO, onUpdateDialogData = {
                    dialogData = it
                })
                coroutineScope.launch {
                    refresh()
                    logger.log("Media loaded")
                    isRefreshing = false
                }
            },
            onDenied = {
                onOutput(MediaStoreComponent.Output.NavigationRequested)
            }
        )
    }

    private fun requestLoadPermission() {
        permissionValidator.requestPermission(
            permission = Manifest.permission.READ_EXTERNAL_STORAGE,
            onUpdateDialogData = {
                dialogData = it
            },
            onGranted = {
                coroutineScope.launch {
                    refresh()
                    logger.log("Media loaded")
                    isRefreshing = false
                }
            },
            onDenied = {
                onOutput(MediaStoreComponent.Output.NavigationRequested)
            }
        )
    }
}