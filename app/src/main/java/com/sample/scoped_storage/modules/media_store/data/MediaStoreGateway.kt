package com.sample.scoped_storage.modules.media_store.data

import android.graphics.Bitmap
import android.net.Uri
import com.sample.scoped_storage.core.utils.TypeFilter
import com.sample.scoped_storage.modules.media_store.domain.DetailedMediaFile
import com.sample.scoped_storage.modules.media_store.domain.MediaFile

interface MediaStoreGateway {

    companion object {
        const val API_30_MULTIPLY_WRITE_PERMISSIONS_ENABLED = true
    }

    suspend fun loadMediaFiles(filter: TypeFilter = TypeFilter.All): List<MediaFile>

    suspend fun writeImage(fileName: String, bitmap: Bitmap)

    suspend fun openMediaFile(uri: Uri): DetailedMediaFile?

    suspend fun removeMediaFile(uri: Uri, onFileRemoved: (FileRemoveResult) -> Unit)

    enum class FileRemoveResult {
        Completed, PermissionDenied, Error
    }
}