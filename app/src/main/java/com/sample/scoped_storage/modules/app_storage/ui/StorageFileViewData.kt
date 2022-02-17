package com.sample.scoped_storage.modules.app_storage.ui

import com.sample.scoped_storage.modules.app_storage.data.models.StorageFile

class StorageFileViewData(
    val name: String
) {
    companion object {

        fun mocks(): List<StorageFileViewData> {
            return List(
                5
            ) {
                StorageFileViewData(name = "test.txt")
            }
        }
    }
}

fun StorageFile.toViewData() = StorageFileViewData(
    name = name
)