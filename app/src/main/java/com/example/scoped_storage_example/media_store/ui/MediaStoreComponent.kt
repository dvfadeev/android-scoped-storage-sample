package com.example.scoped_storage_example.media_store.ui

interface MediaStoreComponent {

    var mediaFiles: List<MediaFileViewData>?

    fun onLoadMedia()
}