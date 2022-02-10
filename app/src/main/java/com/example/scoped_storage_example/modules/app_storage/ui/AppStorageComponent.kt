package com.example.scoped_storage_example.modules.app_storage.ui

interface AppStorageComponent {

    val isInternalStorage: Boolean

    val availableSpace: Long

    val files: List<StorageFileViewData>

    val selectedFile: StorageFileContentViewData?

    val isShowFileContent: Boolean

    fun onToggleStorageClick()

    fun onSaveLogClick()

    fun onFileOpenClick(fileName: String)

    fun onFileRemoveClick(fileName: String)
}