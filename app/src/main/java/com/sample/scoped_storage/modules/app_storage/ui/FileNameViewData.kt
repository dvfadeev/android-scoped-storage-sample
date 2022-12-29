package com.sample.scoped_storage.modules.app_storage.ui

import com.sample.scoped_storage.modules.app_storage.domain.FileName

class FileNameViewData(
    val name: String
) {
    companion object {

        fun mocks(): List<FileNameViewData> {
            return List(
                5
            ) {
                FileNameViewData(name = "test.txt")
            }
        }
    }
}

fun FileName.toViewData() = FileNameViewData(
    name = name
)