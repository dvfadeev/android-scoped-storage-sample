package com.sample.scoped_storage.modules.app_storage.data

import android.content.Context
import android.os.Build
import android.os.StatFs
import android.os.storage.StorageManager
import androidx.core.content.getSystemService
import com.sample.scoped_storage.modules.app_storage.domain.FileName
import com.sample.scoped_storage.modules.app_storage.domain.FileNameWithContent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.util.*

/**
 * You can interact with external app storage only through the File API
 * To get external storage directory use context.getExternalFilesDir.
 */
class AppStorageGatewayExternal(private val context: Context) : AppStorageGateway {

    private val rootFile: File
        get() = context.getExternalFilesDir(null)!!

    private val rootDir: String
        get() = rootFile.absolutePath + File.separator

    /**
     * Write file to external storage
     */
    override suspend fun writeFile(fileName: String, type: String, content: String) = withContext(Dispatchers.IO) {
        val file = File(rootFile, fileName.makeTyped(type))
        file.writeText(content)
    }

    /**
     * Open external storage file
     * @return storage file content
     * @throws java.io.FileNotFoundException
     */
    override suspend fun openFile(fileName: String): FileNameWithContent {
        return FileNameWithContent(
            name = fileName,
            content = File(rootDir + fileName).readText()
        )
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
     * @return list of all storage files
     */
    override suspend fun getFilesList(): List<FileName> {
        return rootFile.listFiles()?.map { it.name }?.map { FileName(name = it) } ?: listOf()
    }

    /**
     * Get available external storage space
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