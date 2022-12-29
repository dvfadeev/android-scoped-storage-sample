package com.sample.scoped_storage.modules.media_store.ui

import android.net.Uri
import com.sample.scoped_storage.core.data.FILE_DATE_PATTERN
import com.sample.scoped_storage.modules.media_store.domain.MediaFile
import java.text.SimpleDateFormat
import java.util.*

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

fun MediaFile.toViewData(): MediaFileViewData {
    val dateFormatter = SimpleDateFormat(FILE_DATE_PATTERN, Locale.getDefault())

    return MediaFileViewData(
        uri = uri,
        name = name,
        type = type,
        size = sizeKb.toString() + "KB",
        date = dateFormatter.format(Date(date))
    )
}