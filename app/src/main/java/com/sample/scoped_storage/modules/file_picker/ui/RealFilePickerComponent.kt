package com.sample.scoped_storage.modules.file_picker.ui

import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.arkivanov.decompose.ComponentContext
import com.sample.scoped_storage.R
import com.sample.scoped_storage.core.data.ComponentToast
import com.sample.scoped_storage.core.data.Logger
import com.sample.scoped_storage.core.ui.widgets.DialogData
import com.sample.scoped_storage.core.ui.widgets.EditTextDialogData
import com.sample.scoped_storage.core.utils.TypeFilter
import com.sample.scoped_storage.core.utils.componentCoroutineScope
import com.sample.scoped_storage.modules.file_picker.data.FilePickerGateway
import com.sample.scoped_storage.modules.media_store.data.MediaStoreGateway
import kotlinx.coroutines.launch

class RealFilePickerComponent(
    componentContext: ComponentContext,
    private val componentToast: ComponentToast,
    private val logger: Logger,
    private val filePicker: FilePickerGateway,
    private val mediaStore: MediaStoreGateway
) : ComponentContext by componentContext, FilePickerComponent {

    private val coroutineScope = componentCoroutineScope()

    override var dialogData: DialogData? by mutableStateOf(null)

    override var filter: TypeFilter by mutableStateOf(TypeFilter.All)

    override var documentFiles: List<FileViewData> by mutableStateOf(listOf())

    init {
        logger.log("Init FilePicker")
    }

    override fun onChangeFilter(filter: TypeFilter) {
        this.filter = filter
    }

    override fun onOpenFileClick(uri: Uri, isDocument: Boolean) {
        coroutineScope.launch {
            if (isDocument) {
                loadDocument(uri)
            } else {
                loadMediaFile(uri)
            }
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
            documentFiles = listOf(it.toFileViewData())
            logger.log("Document file opened")
        } ?: componentToast.show(R.string.file_picker_document_file_open_fail)
    }

    private suspend fun loadMediaFile(uri: Uri) {
        mediaStore.openMediaFile(uri)?.let {
            documentFiles = listOf(it.toFileViewData())
            logger.log("Media file opened")
        } ?: componentToast.show(R.string.file_picker_media_file_open_fail)
    }

    private suspend fun loadDocuments(uris: List<Uri>) {
        documentFiles = filePicker.openDocuments(uris).map { it.toFileViewData() }
        logger.log("Files opened")
    }
}