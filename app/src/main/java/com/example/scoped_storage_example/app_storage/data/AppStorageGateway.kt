package com.example.scoped_storage_example.app_storage.data

interface AppStorageGateway {

    suspend fun writeFile(fileName: String, type: String, content: String)

    suspend fun openFile(fileName: String): String

    suspend fun removeFile(fileName: String)

    suspend fun getFilesList(): List<String>

    suspend fun getAvailableSpaceMb(): Long

    fun String.makeTyped(type: String): String {
        return "$this.$type"
    }
}