package com.example.scoped_storage_example.file_picker.ui

import android.net.Uri
import com.example.scoped_storage_example.file_picker.data.models.DocumentFile
import java.text.SimpleDateFormat
import java.util.*

private const val FILE_DATE_PATTERN = "dd.MM.yyyy HH:mm:ss"

data class DocumentFileViewData(
    val uri: Uri,
    val name: String,
    val id: String,
    val flags: String,
    val mimeType: String,
    val sizeKb: String,
    val dateModified: String?,
    val icon: String?,
    val summary: String?
)

fun DocumentFile.toViewData(): DocumentFileViewData {
    val dateFormatter = SimpleDateFormat(FILE_DATE_PATTERN, Locale.getDefault())

    return DocumentFileViewData(
        uri = uri,
        name = name,
        id = id,
        flags = flags.toString(),
        mimeType = mimeType,
        sizeKb = sizeKb.toString() + "KB",
        dateModified = dateFormatter.format(Date(dateModified)),
        icon = if (icon == 0) {
            null
        } else {
            icon.toString()
        },
        summary = summary
    )
}