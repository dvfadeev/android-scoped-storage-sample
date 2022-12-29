package com.sample.scoped_storage.modules.media_store.domain

import android.net.Uri

data class MediaFile(
    val uri: Uri?,
    val name: String,
    val type: String,
    val sizeKb: Long,
    val date: Long
)