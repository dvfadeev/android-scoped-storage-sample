package com.sample.scoped_storage.modules.file_picker.data.models

import android.net.Uri

data class DocumentFile(
    val uri: Uri,
    val name: String,
    val id: String,
    val flags: Int,
    val mimeType: String,
    val sizeKb: Long,
    val dateModified: Long,
    val icon: Int?,
    val summary: String?
)