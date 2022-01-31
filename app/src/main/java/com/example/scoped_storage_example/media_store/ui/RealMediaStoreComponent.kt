package com.example.scoped_storage_example.media_store.ui

import android.graphics.Bitmap
import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.arkivanov.decompose.ComponentContext
import com.example.scoped_storage_example.core.data.gateway.current_time.CurrentTime
import com.example.scoped_storage_example.core.data.gateway.logger.Logger
import com.example.scoped_storage_example.core.utils.componentCoroutineScope
import com.example.scoped_storage_example.media_store.data.MediaStoreGateway
import com.example.scoped_storage_example.media_store.data.MediaType
import kotlinx.coroutines.launch

class RealMediaStoreComponent(
    componentContext: ComponentContext,
    private val logger: Logger,
    private val currentTime: CurrentTime,
    private val mediaStore: MediaStoreGateway
) : ComponentContext by componentContext, MediaStoreComponent {

    private val coroutineScope = componentCoroutineScope()

    override var mediaType: MediaType by mutableStateOf(MediaType.All)

    override var mediaFiles: List<MediaFileViewData>? by mutableStateOf(null)

    init {
        logger.log("Init AppStorageComponent")
    }

    override fun onLoadMedia() {
        coroutineScope.launch {
            refresh()
            logger.log("Media loaded")
        }
    }

    override fun onSaveBitmap(bitmap: Bitmap) {
        coroutineScope.launch {
            val fileName = "photo " + currentTime.currentTimeString
            mediaStore.savePhoto(fileName, bitmap)
            logger.log("$fileName saved")
            refresh()
        }
    }

    override fun onChangeMediaType(mediaType: MediaType) {
        this.mediaType = mediaType
        onLoadMedia()
    }

    override fun onFileRemoveClick(uri: Uri) {
        coroutineScope.launch {
            mediaStore.removeMediaFile(uri)
            refresh()
        }
    }

    private suspend fun refresh() {
        mediaFiles = mediaStore.loadMediaFiles(mediaType).map { it.toViewData() }
    }
}