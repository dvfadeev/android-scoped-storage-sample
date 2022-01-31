package com.example.scoped_storage_example.media_store.data

import android.graphics.Bitmap

interface MediaStoreGateway {

    suspend fun load(mediaType: MediaType = MediaType.All): List<MediaFile>

    suspend fun savePhoto(fileName: String, bitmap: Bitmap)
}