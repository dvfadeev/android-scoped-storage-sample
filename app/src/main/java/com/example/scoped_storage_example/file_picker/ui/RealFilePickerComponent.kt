package com.example.scoped_storage_example.file_picker.ui

import android.net.Uri
import com.arkivanov.decompose.ComponentContext
import com.example.scoped_storage_example.core.data.gateway.logger.Logger
import com.example.scoped_storage_example.core.utils.componentCoroutineScope
import com.example.scoped_storage_example.file_picker.data.FilePickerGateway
import kotlinx.coroutines.launch

class RealFilePickerComponent(
    componentContext: ComponentContext,
    private val logger: Logger,
    private val mediaStore: FilePickerGateway
) : ComponentContext by componentContext, FilePickerComponent {

    private val coroutineScope = componentCoroutineScope()

    init {
        logger.log("Init FilePicker")
    }

    override fun onOpenFile(uri: Uri) {
        coroutineScope.launch {
            val file = mediaStore.openDocument(uri)
            logger.log("File opened")
        }
    }
}