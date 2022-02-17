package com.sample.scoped_storage.modules.media_store.data

import android.graphics.Bitmap
import android.net.Uri
import com.sample.scoped_storage.core.utils.TypeFilter
import com.sample.scoped_storage.modules.media_store.data.models.DetailedMediaFile
import com.sample.scoped_storage.modules.media_store.data.models.MediaFile

interface MediaStoreGateway {

    companion object {
        const val API_30_MULTIPLY_WRITE_PERMISSIONS_ENABLED = true
        const val WRITE_PERMISSION_REQUEST_ID = 3
    }

    suspend fun loadMediaFiles(filter: TypeFilter = TypeFilter.All): List<MediaFile>

    suspend fun writeImage(fileName: String, bitmap: Bitmap)

    suspend fun openMediaFile(uri: Uri): DetailedMediaFile?

    suspend fun removeMediaFile(uri: Uri): Boolean
}