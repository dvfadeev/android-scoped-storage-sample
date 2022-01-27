package com.example.scoped_storage_example.app_storage.ui

interface AppStorageComponent {

    var isInternalStorage: Boolean

    var files: List<FileViewData>

    var selectedFile: FileContentViewData?

    var isShowFileContent: Boolean

    fun onToggleStorageClick()

    fun onSaveLogClick()

    fun onFileOpenClick(fileName: String)

    fun onFileRemoveClick(fileName: String)
}