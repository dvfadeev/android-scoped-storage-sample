package com.example.scoped_storage_example.media_store.data

import android.net.Uri

data class DetailedMediaFile(
    val uri: Uri,
    val name: String,
    val title: String,
    val path: String,
    val mimeType: String,
    val sizeKb: Int,
    val dateAdded: Long,
    val dateTaken: Long,
    val description: String,
    val height: String?,
    val width: String?,
    val duration: Long?
)