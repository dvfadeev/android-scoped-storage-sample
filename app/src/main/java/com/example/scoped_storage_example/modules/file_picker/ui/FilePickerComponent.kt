package com.example.scoped_storage_example.modules.file_picker.ui

import android.net.Uri
import com.example.scoped_storage_example.core.utils.TypeFilter

interface FilePickerComponent {

    val filter: TypeFilter

    val documentFiles: List<DocumentFileViewData>?

    fun onChangeFilter(filter: TypeFilter)

    fun onOpenFile(uri: Uri)

    fun onOpenFiles(uris: List<Uri>)
}