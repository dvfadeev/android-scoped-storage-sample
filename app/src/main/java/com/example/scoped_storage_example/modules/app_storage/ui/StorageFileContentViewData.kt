package com.example.scoped_storage_example.modules.app_storage.ui

import com.example.scoped_storage_example.modules.app_storage.data.models.StorageFileContent

class StorageFileContentViewData(
    val name: String,
    val content: String
)

fun StorageFileContent.toViewData() = StorageFileContentViewData(
    name = name,
    content = content
)