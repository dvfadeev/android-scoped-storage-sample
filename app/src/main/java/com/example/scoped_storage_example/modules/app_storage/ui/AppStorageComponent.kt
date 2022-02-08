package com.example.scoped_storage_example.modules.app_storage.ui

interface AppStorageComponent {

    var isInternalStorage: Boolean

    var availableSpace: Long

    var files: List<StorageFileViewData>

    var selectedFile: StorageFileContentViewData?

    var isShowFileContent: Boolean

    fun onToggleStorageClick()

    fun onSaveLogClick()

    fun onFileOpenClick(fileName: String)

    fun onFileRemoveClick(fileName: String)
}