package com.example.scoped_storage_example.app_storage.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.arkivanov.decompose.ComponentContext
import com.example.scoped_storage_example.app_storage.data.AppStorageGateway
import com.example.scoped_storage_example.core.data.gateway.logger.Logger

class RealAppStorageComponent(
    componentContext: ComponentContext,
    private val logger: Logger,
    private val appStorage: AppStorageGateway
) : ComponentContext by componentContext, AppStorageComponent {

    override var files: List<FileViewData> by mutableStateOf(listOf())

    override var isShowFileContent: Boolean by mutableStateOf(false)

    override var selectedFile: FileContentViewData? by mutableStateOf(null)

    init {
        logger.log("Init AppStorageComponent")
        refreshFiles()
        backPressedHandler.register(::onBackPressed)
    }

    override fun onAddLogClick() {
        logger.log("Test button clicked!")
    }

    override fun onSaveLogClick() {
        val session = logger.getSession()
        appStorage.writeTextFile(
            fileName = "log " + session.time,
            content = session.logs
        )
        refreshFiles()
    }

    override fun onFileOpenClick(fileName: String) {
        isShowFileContent = true
        selectedFile = FileContentViewData(
            name = fileName,
            content = appStorage.openFile(fileName = fileName)
        )
    }

    override fun onFileRemoveClick(fileName: String) {
        appStorage.removeFile(fileName)
        refreshFiles()
    }

    private fun refreshFiles() {
        files = appStorage.getFilesList().map { FileViewData(name = it) }
    }

    private fun onBackPressed(): Boolean {
        return if (isShowFileContent) {
            isShowFileContent = false
            true
        } else {
            false
        }
    }
}