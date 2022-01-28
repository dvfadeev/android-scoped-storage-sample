package com.example.scoped_storage_example.media_store.ui

import com.arkivanov.decompose.ComponentContext
import com.example.scoped_storage_example.core.data.gateway.logger.Logger
import com.example.scoped_storage_example.core.utils.componentCoroutineScope
import com.example.scoped_storage_example.media_store.data.MediaStoreGateway
import kotlinx.coroutines.launch

class RealMediaStoreComponent(
    componentContext: ComponentContext,
    private val logger: Logger,
    private val mediaStore: MediaStoreGateway
) : ComponentContext by componentContext, MediaStoreComponent {

    private val coroutineScope = componentCoroutineScope()

    init {
        logger.log("Init AppStorageComponent")
    }

    override fun onLoadMedia() {
        coroutineScope.launch {
            val mediaFiles = mediaStore.load()
            logger.log("Media loaded")
        }
    }
}