package com.example.scoped_storage_example.modules.media_store.data

import android.graphics.Bitmap
import android.net.Uri
import com.example.scoped_storage_example.core.utils.TypeFilter
import com.example.scoped_storage_example.modules.media_store.data.models.DetailedMediaFile
import com.example.scoped_storage_example.modules.media_store.data.models.MediaFile

interface MediaStoreGateway {

    companion object {
        const val WRITE_PERMISSION_REQUEST_ID = 3
    }

    suspend fun loadMediaFiles(filter: TypeFilter = TypeFilter.All): List<MediaFile>

    suspend fun writeImage(fileName: String, bitmap: Bitmap)

    suspend fun openMediaFile(uri: Uri): DetailedMediaFile?

    suspend fun removeMediaFile(uri: Uri): Boolean
}