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

    override var selectedMediaFIle: DetailedImageFileViewData? by mutableStateOf(null)

    override var isShowImageFileContent: Boolean by mutableStateOf(false)

    override var isCameraRequested: Boolean by mutableStateOf(false)

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


    override fun onCameraRequest() {
        isCameraRequested = true
    }

    override fun onResetCameraRequest() {
        isCameraRequested = false
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
        coroutineScope.launch {
            mediaStore.openMediaFile(uri)?.let {
                selectedMediaFIle = it.toViewData()
                isShowImageFileContent = true
                logger.log("Image ${it.name} loaded")
            }
        }
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

            isCameraRequested -> {
                isCameraRequested = false
                true
            }

            else -> false
        }
    }
}