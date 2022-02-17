package com.sample.scoped_storage.modules.app_storage.ui

import com.sample.scoped_storage.modules.app_storage.data.models.StorageFileContent

class StorageFileContentViewData(
    val name: String,
    val content: String
)

fun StorageFileContent.toViewData() = StorageFileContentViewData(
    name = name,
    content = content
)