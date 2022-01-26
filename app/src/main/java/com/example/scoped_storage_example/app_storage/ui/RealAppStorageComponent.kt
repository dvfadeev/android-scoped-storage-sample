package com.example.scoped_storage_example.app_storage.ui

import com.arkivanov.decompose.ComponentContext
import com.example.scoped_storage_example.core.data.logger.Logger

class RealAppStorageComponent(
    componentContext: ComponentContext,
    logger: Logger
) : ComponentContext by componentContext, AppStorageComponent {

    init {
        logger.log("Init AppStorageComponent")
    }
}