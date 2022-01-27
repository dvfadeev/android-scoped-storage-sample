package com.example.scoped_storage_example.app_storage.ui

class FileViewData(
    val name: String
) {
    companion object {

        fun mocks(): List<FileViewData> {
            return List(
                5
            ) {
                FileViewData(name = "test.txt")
            }
        }
    }
}