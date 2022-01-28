package com.example.scoped_storage_example.media_store.ui

import android.net.Uri
import com.example.scoped_storage_example.media_store.data.MediaFile

data class MediaFileViewData(
    val uri: Uri?,
    val name: String,
    val type: String,
    val size: String,
    val date: String
) {
    companion object {
        val MOCK = MediaFileViewData(
            uri = null,
            name = "file.txt",
            type = "text",
            size = "51KB",
            date = "12"
        )
    }
}

fun MediaFile.toViewData() = MediaFileViewData(
    uri = uri,
    name = name,
    type = type,
    size = sizeKb.toString() + "KB",
    date = date
)