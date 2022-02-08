package com.example.scoped_storage_example.modules.app_storage.ui

import com.example.scoped_storage_example.modules.app_storage.data.models.StorageFile

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