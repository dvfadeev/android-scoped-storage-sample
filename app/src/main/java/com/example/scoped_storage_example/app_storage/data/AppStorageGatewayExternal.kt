package com.example.scoped_storage_example.app_storage.data

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

/**
 * You can interact with external app storage only through the File API
 * To get external storage directory use context.getExternalFilesDir.
 */
class AppStorageGatewayExternal(private val context: Context) : AppStorageGateway {

    private val rootFile: File?
        get() = context.getExternalFilesDir(null)

    private val rootDir: String
        get() = rootFile?.absolutePath + File.separator

    /**
     * Write file to external storage
     */
    override suspend fun writeFile(fileName: String, type: String, content: String) = withContext(Dispatchers.IO) {
        val file = File(rootFile, fileName.makeTyped(type))
        file.writeText(content)
    }

    /**
     * Open external storage file
     * @return file content in String format
     * @throws java.io.FileNotFoundException
     */
    override suspend fun openFile(fileName: String): String {
        return File(rootDir + fileName).readText()
    }

    /**
     * Remove external storage file
     * @throws java.io.FileNotFoundException
     */
    override suspend fun removeFile(fileName: String) {
        File(rootDir + fileName).delete()
    }

    /**
     * Get a list of all external storage files
     * @return list of all file names
     */
    override suspend fun getFilesList(): List<String> {
        return rootFile?.listFiles()?.map { it.name } ?: listOf()
    }
}