package com.example.scoped_storage_example.app_storage.data

interface AppStorageGateway {

    fun writeTextFile(fileName: String, content: String)

    fun writeFile(fileName: String, content: ByteArray)

    fun openFile(fileName: String): String

    fun removeFile(fileName: String)

    fun getFilesList(): List<String>
}