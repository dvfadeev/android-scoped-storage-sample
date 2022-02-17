package com.sample.scoped_storage.modules.file_picker.ui

import android.net.Uri
import com.sample.scoped_storage.core.ui.widgets.DialogData
import com.sample.scoped_storage.core.utils.TypeFilter

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