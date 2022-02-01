package com.example.scoped_storage_example.file_picker.ui

import android.net.Uri

interface FilePickerComponent {

    var documentFiles: List<DocumentFileViewData>?

    fun onOpenFile(uri: Uri)

    fun onOpenFiles(uris: List<Uri>)
}