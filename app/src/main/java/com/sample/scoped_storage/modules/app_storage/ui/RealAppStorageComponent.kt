package com.sample.scoped_storage.modules.app_storage.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.arkivanov.decompose.ComponentContext
import com.sample.scoped_storage.R
import com.sample.scoped_storage.core.data.ComponentToast
import com.sample.scoped_storage.core.utils.FileTypes
import com.sample.scoped_storage.core.data.Logger
import com.sample.scoped_storage.core.utils.componentCoroutineScope
import com.sample.scoped_storage.modules.app_storage.data.AppStorageGateway
import com.sample.scoped_storage.modules.app_storage.data.AppStorageGatewayExternal
import com.sample.scoped_storage.modules.app_storage.data.AppStorageGatewayInternal
import kotlinx.coroutines.launch

/**
 * Component for AppStorage
 * Uses internal / external storage gateways to get access device storage
 */
class RealAppStorageComponent(
    componentContext: ComponentContext,
    private val componentToast: ComponentToast,
    private val logger: Logger,
    private val internalStorage: AppStorageGatewayInternal,
    private val externalStorage: AppStorageGatewayExternal
) : ComponentContext by componentContext, AppStorageComponent {

    private lateinit var appStorage: AppStorageGateway

    private val coroutineScope = componentCoroutineScope()

    override var isInternalStorage: Boolean by mutableStateOf(true)

    override var availableSpace: Long by mutableStateOf(0)

    override var files: List<StorageFileViewData> by mutableStateOf(listOf())

    override var isShowFileContent: Boolean by mutableStateOf(false)

    override var selectedFile: StorageFileContentViewData? by mutableStateOf(null)

    init {
        logger.log("Init AppStorageComponent")
        setAppStore()
        refreshFiles()
        backPressedHandler.register(::onBackPressed)
    }

    override fun onToggleStorageClick() {
        isInternalStorage = !isInternalStorage
        setAppStore()
        refreshFiles()
        logger.log("Toggle storage to ${if (isInternalStorage) "internal" else "external"}")
    }

    override fun onSaveLogClick() {
        coroutineScope.launch {
            val session = logger.getSession()
            appStorage.writeFile(
                fileName = "log " + session.time,
                type = FileTypes.TYPE_TEXT,
                content = session.logs
            )
            componentToast.show(R.string.app_storage_save_log_completed)
            refreshFiles()
        }
    }

    override fun onFileOpenClick(fileName: String) {
        coroutineScope.launch {
            isShowFileContent = true
            selectedFile = appStorage.openFile(fileName = fileName).toViewData()
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

    private fun setAppStore() {
        appStorage = if (isInternalStorage) {
            externalStorage
        } else {
            internalStorage
        }
    }

    private fun refreshFiles() {
        coroutineScope.launch {
            availableSpace = appStorage.getAvailableSpaceMb()
            files = appStorage.getFilesList().map { it.toViewData() }
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