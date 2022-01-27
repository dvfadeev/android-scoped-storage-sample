package com.example.scoped_storage_example.app_storage.data

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AppStorageGatewayInternal(private val context: Context) : AppStorageGateway {

    override suspend fun writeFile(fileName: String, type: String, content: String) = withContext(Dispatchers.IO) {
        context.openFileOutput(fileName.makeTyped(type), Context.MODE_PRIVATE).use {
            it.write(content.toByteArray())
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
        return context.fileList().toList().sortedBy { it }
    }
}