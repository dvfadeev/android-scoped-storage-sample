package com.example.scoped_storage_example.media_store.data

interface MediaStoreGateway {

    suspend fun load(): List<MediaFile>
}