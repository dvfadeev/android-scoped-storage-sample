package com.example.scoped_storage_example.media_store.ui

import android.net.Uri
import com.example.scoped_storage_example.media_store.data.models.DetailedMediaFile
import java.text.SimpleDateFormat
import java.util.*

private const val FILE_DATE_PATTERN = "dd.MM.yyyy HH:mm:ss"

data class DetailedImageFileViewData(
    val uri: Uri,
    val name: String,
    val title: String,
    val path: String,
    val mimeType: String,
    val size: String,
    val dateAdded: String,
    val dateTaken: String,
    val description: String,
    val resolution: String?,
    val duration: String?
)

fun DetailedMediaFile.toViewData(): DetailedImageFileViewData {
    val dateFormatter = SimpleDateFormat(FILE_DATE_PATTERN, Locale.getDefault())

    return DetailedImageFileViewData(
        uri = uri,
        name = name,
        title = title,
        path = path,
        mimeType = mimeType,
        size = sizeKb.toString() + "KB",
        dateAdded = dateFormatter.format(Date(dateAdded)),
        dateTaken = dateFormatter.format(Date(dateTaken)),
        description = description,
        resolution = if (height != null && width != null) {
            "$height x $width"
        } else {
            null
        },
        duration = if (duration != null) {
            duration.toString() + "s"
        } else {
            null
        }
    )
}

