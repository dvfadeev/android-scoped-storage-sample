package com.example.scoped_storage_example.media_store.data

import android.graphics.Bitmap
import android.net.Uri

interface MediaStoreGateway {

    suspend fun loadMediaFiles(mediaType: MediaType = MediaType.All): List<MediaFile>

    suspend fun savePhoto(fileName: String, bitmap: Bitmap)

    suspend fun removeMediaFile(uri: Uri)
}