package com.sample.scoped_storage.modules.file_picker.ui

import android.net.Uri
import com.sample.scoped_storage.core.data.FILE_DATE_PATTERN
import com.sample.scoped_storage.modules.file_picker.domain.DocumentFile
import com.sample.scoped_storage.modules.media_store.domain.DetailedMediaFile
import java.text.SimpleDateFormat
import java.util.*

data class FileViewData(
    val uri: Uri,
    val name: String,
    val sizeKb: String,
    val isDocument: Boolean,
    val mimeType: String?,
    val dateModified: String?,
    val id: String? = null,
    val flags: String? = null,
    val icon: String? = null,
    val summary: String? = null
)

fun DocumentFile.toFileViewData() = FileViewData(
    uri = uri,
    name = name,
    sizeKb = sizeKb.toString() + "KB",
    isDocument = true,
    mimeType = mimeType,
    dateModified = getFormatter().format(Date(dateModified)),
    id = id,
    flags = flags.toString(),
    icon = if (icon == 0) {
        null
    } else {
        icon.toString()
    },
    summary = summary
)

fun DetailedMediaFile.toFileViewData() = FileViewData(
    uri = uri,
    name = name,
    sizeKb = sizeKb.toString() + "KB",
    isDocument = false,
    mimeType = mimeType,
    dateModified = dateAdded?.let { getFormatter().format(Date(it)) }
)

private fun getFormatter() = SimpleDateFormat(FILE_DATE_PATTERN, Locale.getDefault())