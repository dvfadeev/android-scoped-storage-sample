package com.example.scoped_storage_example.media_store.data.models

import android.net.Uri

data class MediaFile(
    val uri: Uri?,
    val name: String,
    val type: String,
    val sizeKb: Long,
    val date: Long
)