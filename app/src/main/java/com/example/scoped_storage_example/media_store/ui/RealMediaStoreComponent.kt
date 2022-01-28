package com.example.scoped_storage_example.media_store.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.arkivanov.decompose.ComponentContext
import com.example.scoped_storage_example.core.data.gateway.logger.Logger
import com.example.scoped_storage_example.core.utils.componentCoroutineScope
import com.example.scoped_storage_example.media_store.data.MediaStoreGateway
import com.example.scoped_storage_example.media_store.data.MediaType
import kotlinx.coroutines.launch

class RealMediaStoreComponent(
    componentContext: ComponentContext,
    private val logger: Logger,
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
            mediaFiles = mediaStore.load(mediaType).map { it.toViewData() }
            logger.log("Media loaded")
        }
    }

    override fun onChangeMediaType(mediaType: MediaType) {
        this.mediaType = mediaType
        onLoadMedia()
    }
}