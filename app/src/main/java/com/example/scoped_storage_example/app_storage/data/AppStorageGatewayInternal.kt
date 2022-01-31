package com.example.scoped_storage_example.app_storage.data

import android.content.Context
import android.os.Build
import android.os.StatFs
import android.os.storage.StorageManager
import androidx.core.content.getSystemService
import com.example.scoped_storage_example.app_storage.data.models.StorageFile
import com.example.scoped_storage_example.app_storage.data.models.StorageFileContent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.util.*

/**
 * There are two ways to interact with internal app storage:
 * 1. Through the File API
 *    To get external storage directory use context.filesDir.
 * 2. As an alternative, u can call open / remove file from context
 *    That works with a file within the context.filesDir directory.
 */
class AppStorageGatewayInternal(private val context: Context) : AppStorageGateway {

    /**
     * Toggle this flag to enable File API
     */
    private val useFileApi = false

    private val rootFile: File
        get() = context.filesDir

    private val rootDir: String
        get() = rootFile.absolutePath + File.separator

    /**
     * Write file to internal storage
     */
    override suspend fun writeFile(fileName: String, type: String, content: String) = withContext(Dispatchers.IO) {
        if (useFileApi) {
            val file = File(rootFile, fileName.makeTyped(type))
            file.writeText(content)
        } else {
            context.openFileOutput(fileName.makeTyped(type), Context.MODE_PRIVATE).use {
                it.write(content.toByteArray())
            }
        }
    }

    /**
     * Open internal storage file
     * @return storage file content
     * @throws java.io.FileNotFoundException
     */
    override suspend fun openFile(fileName: String): StorageFileContent = withContext(Dispatchers.IO) {
        var content = ""

        if (useFileApi) {
            content = File(rootDir + fileName).readText()
        } else {
            context.openFileInput(fileName).bufferedReader().useLines { lines ->
                content = lines.joinToString(separator = "\n")
            }
        }

        return@withContext StorageFileContent(
            name = fileName,
            content = content
        )
    }

    /**
     * Remove internal storage file
     * @throws java.io.FileNotFoundException
     */
    override suspend fun removeFile(fileName: String) {
        if (useFileApi) {
            File(rootDir + fileName).delete()
        } else {
            context.deleteFile(fileName)
        }
    }

    /**
     * Get a list of all internal storage files
     * @return list of all storage files
     */
    override suspend fun getFilesList(): List<StorageFile> {
        return if (useFileApi) {
            rootFile.listFiles()?.map { it.name }?.map { StorageFile(it) } ?: listOf()
        } else {
            context.fileList().toList().sortedBy { it }.map { StorageFile(it) }
        }
    }

    /**
     * Get available Internal storage space
     * @return free space in MB's
     */
    override suspend fun getAvailableSpaceMb(): Long = withContext(Dispatchers.IO) {
        val availableBytes = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val storageManager = context.getSystemService<StorageManager>()!!
            val appSpecificInternalDirUuid: UUID = storageManager.getUuidForPath(rootFile)
            storageManager.getAllocatableBytes(appSpecificInternalDirUuid)
        } else {
            val stat = StatFs(rootDir)
            stat.availableBlocksLong * stat.blockSizeLong
        }
        return@withContext availableBytes / 1024 / 1024
    }
}