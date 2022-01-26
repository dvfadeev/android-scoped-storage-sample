package com.example.scoped_storage_example.app_storage.data

interface AppStorageGateway {

    suspend fun writeTextFile(fileName: String, content: String)

    suspend fun writeFile(fileName: String, content: ByteArray)

    suspend fun openFile(fileName: String): String

    suspend fun removeFile(fileName: String)

    suspend fun getFilesList(): List<String>
}