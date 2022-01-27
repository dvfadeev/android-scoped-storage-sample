package com.example.scoped_storage_example.app_storage.data

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

class AppStorageGatewayExternal(private val context: Context) : AppStorageGateway {

    private val rootFile: File?
        get() = context.getExternalFilesDir(null)

    private val rootDir: String
        get() = rootFile?.absolutePath + File.separator

    override suspend fun writeFile(fileName: String, type: String, content: String) = withContext(Dispatchers.IO) {
        val file = File(rootFile, fileName.makeTyped(type))
        file.writeText(content)
    }

    override suspend fun openFile(fileName: String): String {
        return File(rootDir + fileName).readText()
    }

    override suspend fun removeFile(fileName: String) {
        File(rootDir + fileName).delete()
    }

    override suspend fun getFilesList(): List<String> {
        return rootFile?.listFiles()?.map { it.name } ?: listOf()
    }
}