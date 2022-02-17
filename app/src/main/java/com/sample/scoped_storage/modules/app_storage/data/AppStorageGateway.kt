package com.sample.scoped_storage.modules.app_storage.data

import com.sample.scoped_storage.modules.app_storage.data.models.StorageFile
import com.sample.scoped_storage.modules.app_storage.data.models.StorageFileContent

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