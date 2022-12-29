package com.sample.scoped_storage.modules.app_storage.data

import com.sample.scoped_storage.modules.app_storage.domain.FileName
import com.sample.scoped_storage.modules.app_storage.domain.FileNameWithContent

interface AppStorageGateway {

    suspend fun writeFile(fileName: String, type: String, content: String)

    suspend fun openFile(fileName: String): FileNameWithContent

    suspend fun removeFile(fileName: String)

    suspend fun getFilesList(): List<FileName>

    suspend fun getAvailableSpaceMb(): Long

    fun String.makeTyped(type: String): String {
        return "$this.$type"
    }
}