package com.example.scoped_storage_example.app_storage.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.arkivanov.decompose.ComponentContext
import com.example.scoped_storage_example.app_storage.data.AppStorageGateway
import com.example.scoped_storage_example.app_storage.data.AppStorageGatewayExternal
import com.example.scoped_storage_example.app_storage.data.AppStorageGatewayInternal
import com.example.scoped_storage_example.core.data.gateway.logger.Logger
import com.example.scoped_storage_example.core.utils.componentCoroutineScope
import kotlinx.coroutines.launch

class RealAppStorageComponent(
    componentContext: ComponentContext,
    private val logger: Logger,
    private val internalStorage: AppStorageGatewayInternal,
    private val externalStorage: AppStorageGatewayExternal
) : ComponentContext by componentContext, AppStorageComponent {

    private var appStorage: AppStorageGateway = externalStorage

    private val coroutineScope = componentCoroutineScope()

    override var files: List<FileViewData> by mutableStateOf(listOf())

    override var isShowFileContent: Boolean by mutableStateOf(false)

    override var selectedFile: FileContentViewData? by mutableStateOf(null)

    init {
        logger.log("Init AppStorageComponent")
        refreshFiles()
        backPressedHandler.register(::onBackPressed)
    }

    override fun onAddLogClick() {
        logger.log("Log button clicked!")
    }

    override fun onSaveLogClick() {
        coroutineScope.launch {
            val session = logger.getSession()
            appStorage.writeFile(
                fileName = "log " + session.time,
                type = AppStorageGateway.TYPE_TEXT,
                content = session.logs
            )
            refreshFiles()
        }
    }

    override fun onFileOpenClick(fileName: String) {
        coroutineScope.launch {
            isShowFileContent = true
            selectedFile = FileContentViewData(
                name = fileName,
                content = appStorage.openFile(fileName = fileName)
            )
            logger.log("File $fileName opened")
        }
    }

    override fun onFileRemoveClick(fileName: String) {
        coroutineScope.launch {
            appStorage.removeFile(fileName)
            logger.log("File $fileName removed")
            refreshFiles()
        }
    }

    private fun refreshFiles() {
        coroutineScope.launch {
            files = appStorage.getFilesList().map { FileViewData(name = it) }
        }
    }

    private fun onBackPressed(): Boolean {
        return if (isShowFileContent) {
            isShowFileContent = false
            logger.log("File content viewer closed")
            true
        } else {
            false
        }
    }
}