package com.sample.scoped_storage.modules.app_storage.ui

interface AppStorageComponent {

    val isInternalStorage: Boolean

    val availableSpace: Long

    val files: List<FileNameViewData>

    val selectedFile: FileNameWithContentViewData?

    val isShowFileContent: Boolean

    fun onToggleStorageClick()

    fun onSaveLogClick()

    fun onFileOpenClick(fileName: String)

    fun onFileRemoveClick(fileName: String)
}