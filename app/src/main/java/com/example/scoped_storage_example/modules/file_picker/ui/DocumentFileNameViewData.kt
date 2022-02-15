package com.example.scoped_storage_example.modules.file_picker.ui

import android.net.Uri

data class DocumentFileNameViewData(
    val uri: Uri,
    val name: String,
)