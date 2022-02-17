package com.example.scoped_storage_example.modules.file_picker.ui

import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.arkivanov.decompose.ComponentContext
import com.example.scoped_storage_example.R
import com.example.scoped_storage_example.core.data.ComponentToast
import com.example.scoped_storage_example.core.data.Logger
import com.example.scoped_storage_example.core.ui.widgets.DialogData
import com.example.scoped_storage_example.core.ui.widgets.EditTextDialogData
import com.example.scoped_storage_example.core.utils.TypeFilter
import com.example.scoped_storage_example.core.utils.componentCoroutineScope
import com.example.scoped_storage_example.modules.file_picker.data.FilePickerGateway
import kotlinx.coroutines.launch

class RealFilePickerComponent(
    componentContext: ComponentContext,
    private val componentToast: ComponentToast,
    private val logger: Logger,
    private val filePicker: FilePickerGateway
) : ComponentContext by componentContext, FilePickerComponent {

    private val coroutineScope = componentCoroutineScope()

    override var dialogData: DialogData? by mutableStateOf(null)

    override var filter: TypeFilter by mutableStateOf(TypeFilter.All)

    override var documentFiles: List<DocumentFileViewData> by mutableStateOf(listOf())

    init {
        logger.log("Init FilePicker")
    }

    override fun onChangeFilter(filter: TypeFilter) {
        this.filter = filter
    }

    override fun onOpenFileClick(uri: Uri) {
        coroutineScope.launch {
            loadDocument(uri)
        }
    }

    override fun onOpenFilesClick(uris: List<Uri>) {
        coroutineScope.launch {
            loadDocuments(uris)
        }
    }

    override fun onOpenRenameDialogClick(uri: Uri) {
        var fileName = documentFiles.find { it.uri == uri }?.name ?: ""

        dialogData = EditTextDialogData(
            initText = fileName,
            onTextChanged = {
                fileName = it
            },
            titleRes = R.string.file_picker_file_rename_title,
            onAcceptClick = {
                coroutineScope.launch {
                    val result = filePicker.renameDocument(uri, fileName)

                    if (result) {
                        loadDocuments(documentFiles.map { it.uri }.filter { it != uri })
                        componentToast.show(R.string.file_picker_file_renamed)
                        logger.log("File renamed")
                    } else {
                        componentToast.show(R.string.file_picker_file_rename_error)
                        logger.log("File rename failed")
                    }

                }
                dialogData = null
            },
            onCancelClick = {
                dialogData = null
            }
        )
    }

    override fun onOpenRemoveDialogClick(uri: Uri) {
        dialogData = DialogData(
            titleRes = R.string.file_picker_file_remove_title,
            messageRes = R.string.file_picker_file_remove_message,
            onAcceptClick = {
                coroutineScope.launch {
                    val result = filePicker.removeDocument(uri)

                    if (result) {
                        componentToast.show(R.string.file_picker_file_removed)
                        loadDocuments(documentFiles.map { it.uri }.filter { it != uri })
                        logger.log("File removed")
                    } else {
                        componentToast.show(R.string.file_picker_file_remove_error)
                        logger.log("File remove failed")
                    }
                }
                dialogData = null
            },
            onCancelClick = {
                dialogData = null
            }
        )
    }

    private suspend fun loadDocument(uri: Uri) {
        filePicker.openDocument(uri)?.let {
            documentFiles = listOf(it.toViewData())
            logger.log("File opened")
        } ?: componentToast.show(R.string.file_picker_file_open_fail)
    }

    private suspend fun loadDocuments(uris: List<Uri>) {
        documentFiles = filePicker.openDocuments(uris).map { it.toViewData() }
        logger.log("Files opened")
    }
}