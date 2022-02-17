package com.sample.scoped_storage.modules.file_picker.ui

import android.net.Uri

data class DocumentFileNameViewData(
    val uri: Uri,
    val name: String,
)