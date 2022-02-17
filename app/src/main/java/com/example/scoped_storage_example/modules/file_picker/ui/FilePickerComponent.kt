package com.example.scoped_storage_example.modules.file_picker.ui

import android.net.Uri
import com.example.scoped_storage_example.core.ui.widgets.DialogData
import com.example.scoped_storage_example.core.utils.TypeFilter

interface FilePickerComponent {

    val dialogData: DialogData?

    val filter: TypeFilter

    val documentFiles: List<DocumentFileViewData>

    fun onChangeFilter(filter: TypeFilter)

    fun onOpenFileClick(uri: Uri)

    fun onOpenFilesClick(uris: List<Uri>)

    fun onOpenRenameDialogClick(uri: Uri)

    fun onOpenRemoveDialogClick(uri: Uri)
}