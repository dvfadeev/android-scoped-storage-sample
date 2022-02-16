package com.example.scoped_storage_example.modules.media_store.ui

import android.Manifest
import android.graphics.Bitmap
import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.arkivanov.decompose.ComponentContext
import com.example.scoped_storage_example.R
import com.example.scoped_storage_example.core.data.ComponentToast
import com.example.scoped_storage_example.core.data.CurrentTime
import com.example.scoped_storage_example.core.data.Logger
import com.example.scoped_storage_example.core.data.PermissionChecker
import com.example.scoped_storage_example.core.ui.widgets.DialogData
import com.example.scoped_storage_example.core.utils.TypeFilter
import com.example.scoped_storage_example.core.utils.componentCoroutineScope
import com.example.scoped_storage_example.modules.media_store.data.MediaStoreGateway
import kotlinx.coroutines.launch

class RealMediaStoreComponent(
    componentContext: ComponentContext,
    private val onOutput: (MediaStoreComponent.Output) -> Unit,
    private val componentToast: ComponentToast,
    private val logger: Logger,
    private val currentTime: CurrentTime,
    private val permissionChecker: PermissionChecker,
    private val mediaStore: MediaStoreGateway
) : ComponentContext by componentContext, MediaStoreComponent {

    private val coroutineScope = componentCoroutineScope()

    override var dialogData: DialogData? by mutableStateOf(null)

    override var filter: TypeFilter by mutableStateOf(TypeFilter.All)

    override var mediaFiles: List<MediaFileViewData>? by mutableStateOf(null)

    override var selectedUri: Uri? by mutableStateOf(null)

    override var selectedMediaFile: DetailedImageFileViewData? by mutableStateOf(null)

    override var isShowImageFileContent: Boolean by mutableStateOf(false)

    override var permissionRequest: PermissionRequest by mutableStateOf(PermissionRequest.ReadStorage)

    init {
        logger.log("Init AppStorageComponent")
        backPressedHandler.register(::onBackPressed)

        onLoadMedia()
    }

    override fun onLoadMedia() {
        requestPermission(
            permission = Manifest.permission.READ_EXTERNAL_STORAGE,
            messageRes = R.string.media_store_read_permission_request,
            onGranted = {
                coroutineScope.launch {
                    refresh()
                    logger.log("Media loaded")
                }
            },
            onDenied =  {
                onOutput(MediaStoreComponent.Output.NavigationRequested)
            }
        )
    }

    override fun onRequestPermission(permissionRequest: PermissionRequest) {
        this.permissionRequest = permissionRequest
    }

    override fun onSaveBitmap(bitmap: Bitmap) {
        coroutineScope.launch {
            val fileName = "photo " + currentTime.currentTimeString
            mediaStore.writeImage(fileName, bitmap)
            logger.log("$fileName saved")
            componentToast.show(R.string.media_store_take_photo_completed)
            refresh()
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
        coroutineScope.launch {
            if (mediaStore.removeMediaFile(uri)) {
                refresh()
                componentToast.show(R.string.media_store_file_remove_completed)
            } else {
                componentToast.show(R.string.media_store_file_remove_error)
            }
        }
    }

    private suspend fun refresh() {
        mediaFiles = mediaStore.loadMediaFiles(filter).map { it.toViewData() }
    }

    private fun onBackPressed(): Boolean {
        return when {
            isShowImageFileContent -> {
                isShowImageFileContent = false
                logger.log("Media file content viewer closed")
                true
            }
            permissionRequest == PermissionRequest.TakePhoto || permissionRequest == PermissionRequest.RemoveFile -> {
                permissionRequest = PermissionRequest.ReadStorage
                true
            }
            else -> false
        }
    }

    private fun requestPermission(
        permission: String,
        messageRes: Int,
        onGranted: (() -> Unit),
        onDenied: (() -> Unit)? = null
    ) {
        coroutineScope.launch {
            val permissionRequestedDialog = DialogData(
                titleRes = R.string.media_store_permission_request_title,
                messageRes = messageRes,
                onAcceptClick = {
                    requestPermission(permission = permission, messageRes = messageRes, onGranted = onGranted, onDenied = onDenied)
                },
                onCancelClick = {
                    dialogData = null
                    onDenied?.invoke()
                }
            )

            val permissionDeniedDialog = DialogData(
                titleRes = R.string.media_store_permission_denied_title,
                messageRes = R.string.media_store_permission_denied,
                onAcceptClick = {
                    dialogData = null
                    onDenied?.invoke()
                }
            )

            when (permissionChecker.checkPermission(permission)) {
                PermissionChecker.Result.Granted -> {
                    dialogData = null
                    onGranted.invoke()
                }
                PermissionChecker.Result.Denied -> {
                    dialogData = permissionDeniedDialog
                }
                PermissionChecker.Result.ShowMessage -> {
                    dialogData = permissionRequestedDialog
                }
            }
        }
    }
}