package com.example.scoped_storage_example.app_storage.ui

import com.arkivanov.decompose.ComponentContext
import com.example.scoped_storage_example.app_storage.data.AppStorageGateway
import com.example.scoped_storage_example.core.data.gateway.logger.Logger

class RealAppStorageComponent(
    componentContext: ComponentContext,
    private val logger: Logger,
    private val appStorage: AppStorageGateway
) : ComponentContext by componentContext, AppStorageComponent {

    init {
        logger.log("Init AppStorageComponent")
    }

    override fun onAddLogClick() {
        logger.log("Test button clicked!")
    }

    override fun onSaveLogClick() {
        val session = logger.getSession()
        appStorage.writeTextFile(
            fileName = session.time,
            content = session.logs
        )
    }
}