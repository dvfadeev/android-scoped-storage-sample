package com.sample.scoped_storage.modules.app_storage.ui

import com.sample.scoped_storage.modules.app_storage.domain.FileNameWithContent

class FileNameWithContentViewData(
    val name: String,
    val content: String
)

fun FileNameWithContent.toViewData() = FileNameWithContentViewData(
    name = name,
    content = content
)