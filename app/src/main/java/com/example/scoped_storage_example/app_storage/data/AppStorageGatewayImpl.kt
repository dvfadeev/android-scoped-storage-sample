package com.example.scoped_storage_example.app_storage.data

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AppStorageGatewayImpl(private val context: Context) : AppStorageGateway {

    companion object {
        private const val TYPE_TEXT = ".txt"
    }

    override suspend fun writeTextFile(fileName: String, content: String) {
        writeFile(fileName + TYPE_TEXT, content.toByteArray())
    }

    override suspend fun writeFile(fileName: String, content: ByteArray) = withContext(Dispatchers.IO) {
        context.openFileOutput(fileName, Context.MODE_PRIVATE).use {
            it.write(content)
        }
    }

    override suspend fun openFile(fileName: String): String = withContext(Dispatchers.IO) {
        var content = ""
        context.openFileInput(fileName).bufferedReader().useLines { lines ->
            content = lines.joinToString(separator = "\n")
        }
        return@withContext content
    }

    override suspend fun removeFile(fileName: String) {
        context.deleteFile(fileName)
    }

    override suspend fun getFilesList(): List<String> {
        return context.fileList().toList()
    }
}