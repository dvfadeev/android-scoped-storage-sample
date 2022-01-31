package com.example.scoped_storage_example.media_store.ui

import android.graphics.Bitmap
import com.example.scoped_storage_example.media_store.data.MediaType

interface MediaStoreComponent {

    var mediaType: MediaType

    var mediaFiles: List<MediaFileViewData>?

    fun onLoadMedia()

    fun onSaveBitmap(bitmap: Bitmap)

    fun onChangeMediaType(mediaType: MediaType)
}