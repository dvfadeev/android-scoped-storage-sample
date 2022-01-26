package com.example.scoped_storage_example.app_storage.ui

interface AppStorageComponent {

    var files: List<FileViewData>

    var selectedFile: FileContentViewData?

    var isShowFileContent: Boolean

    fun onAddLogClick()

    fun onSaveLogClick()

    fun onFileOpenClick(fileName: String)

    fun onFileRemoveClick(fileName: String)
}