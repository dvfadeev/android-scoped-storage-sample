package com.example.scoped_storage_example.media_store.data

import android.net.Uri

data class MediaFile(
    val uri: Uri?,
    val name: String,
    val type: String,
    val sizeKb: Int,
    val date: Long,
    val path: String
)