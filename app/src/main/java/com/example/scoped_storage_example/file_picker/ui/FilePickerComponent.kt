package com.example.scoped_storage_example.file_picker.ui

import android.net.Uri

interface FilePickerComponent {

    fun onOpenFile(uri: Uri)
}