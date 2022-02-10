package com.example.scoped_storage_example.modules.media_store.ui

import android.graphics.Bitmap
import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.arkivanov.decompose.ComponentContext
import com.example.scoped_storage_example.core.data.CurrentTime
import com.example.scoped_storage_example.core.data.Logger
import com.example.scoped_storage_example.core.utils.TypeFilter
import com.example.scoped_storage_example.core.utils.componentCoroutineScope
import com.example.scoped_storage_example.modules.media_store.data.MediaStoreGateway
import kotlinx.coroutines.launch

class RealMediaStoreComponent(
    componentContext: ComponentContext,
    private val logger: Logger,
    private val currentTime: CurrentTime,
    private val mediaStore: MediaStoreGateway
) : ComponentContext by componentContext, MediaStoreComponent {

    private val coroutineScope = componentCoroutineScope()

    override var filter: TypeFilter by mutableStateOf(TypeFilter.All)

    override var mediaFiles: List<MediaFileViewData>? by mutableStateOf(null)

    override var selectedUri: Uri? by mutableStateOf(null)

    override var selectedMediaFile: DetailedImageFileViewData? by mutableStateOf(null)

    override var isShowImageFileContent: Boolean by mutableStateOf(false)

    override var permissionRequest: PermissionRequest by mutableStateOf(PermissionRequest.ReadStorage)

    init {
        logger.log("Init AppStorageComponent")
        backPressedHandler.register(::onBackPressed)
    }

    override fun onLoadMedia() {
        coroutineScope.launch {
            refresh()
            logger.log("Media loaded")
        }
    }

    override fun onRequestPermission(permissionRequest: PermissionRequest) {
        this.permissionRequest = permissionRequest
    }

    override fun onSaveBitmap(bitmap: Bitmap) {
        coroutineScope.launch {
            val fileName = "photo " + currentTime.currentTimeString
            mediaStore.writeImage(fileName, bitmap)
            logger.log("$fileName saved")
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
            }
        }
    }

    override fun onFileLongClick(uri: Uri) {
        selectedUri = uri
    }

    override fun onFileRemoveClick(uri: Uri) {
        coroutineScope.launch {
            mediaStore.removeMediaFile(uri)
            refresh()
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
}