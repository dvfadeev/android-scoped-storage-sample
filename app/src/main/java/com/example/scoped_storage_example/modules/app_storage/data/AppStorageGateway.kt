package com.example.scoped_storage_example.modules.app_storage.data

import com.example.scoped_storage_example.modules.app_storage.data.models.StorageFile
import com.example.scoped_storage_example.modules.app_storage.data.models.StorageFileContent

interface AppStorageGateway {

    suspend fun writeFile(fileName: String, type: String, content: String)

    suspend fun openFile(fileName: String): StorageFileContent

    suspend fun removeFile(fileName: String)

    suspend fun getFilesList(): List<StorageFile>

    suspend fun getAvailableSpaceMb(): Long

    fun String.makeTyped(type: String): String {
        return "$this.$type"
    }
}