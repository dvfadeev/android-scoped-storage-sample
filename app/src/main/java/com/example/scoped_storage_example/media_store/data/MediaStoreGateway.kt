package com.example.scoped_storage_example.media_store.data

import android.graphics.Bitmap
import android.net.Uri
import com.example.scoped_storage_example.media_store.data.models.DetailedMediaFile
import com.example.scoped_storage_example.media_store.data.models.MediaFile
import com.example.scoped_storage_example.media_store.data.models.MediaType

interface MediaStoreGateway {

    suspend fun loadMediaFiles(mediaType: MediaType = MediaType.All): List<MediaFile>

    suspend fun writeImage(fileName: String, bitmap: Bitmap)

    suspend fun openMediaFile(uri: Uri): DetailedMediaFile?

    suspend fun removeMediaFile(uri: Uri)
}