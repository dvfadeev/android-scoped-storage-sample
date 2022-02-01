package com.example.scoped_storage_example.file_picker.ui

import com.arkivanov.decompose.ComponentContext
import com.example.scoped_storage_example.core.data.gateway.logger.Logger

class RealFilePickerComponent(
    componentContext: ComponentContext,
    private val logger: Logger
) : ComponentContext by componentContext, FilePickerComponent {

    init {
        logger.log("Init FilePicker")
    }
}