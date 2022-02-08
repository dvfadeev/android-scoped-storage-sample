package com.example.scoped_storage_example.modules.file_picker.ui

import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.arkivanov.decompose.ComponentContext
import com.example.scoped_storage_example.core.data.Logger
import com.example.scoped_storage_example.core.utils.TypeFilter
import com.example.scoped_storage_example.core.utils.componentCoroutineScope
import com.example.scoped_storage_example.modules.file_picker.data.FilePickerGateway
import kotlinx.coroutines.launch

class RealFilePickerComponent(
    componentContext: ComponentContext,
    private val logger: Logger,
    private val mediaStore: FilePickerGateway
) : ComponentContext by componentContext, FilePickerComponent {

    private val coroutineScope = componentCoroutineScope()

    override var filter: TypeFilter by mutableStateOf(TypeFilter.All)

    override var documentFiles: List<DocumentFileViewData>? by mutableStateOf(null)

    init {
        logger.log("Init FilePicker")
    }

    override fun onChangeFilter(filter: TypeFilter) {
        this.filter = filter
    }

    override fun onOpenFile(uri: Uri) {
        coroutineScope.launch {
            mediaStore.openDocument(uri)?.let {
                documentFiles = listOf(it.toViewData())
                logger.log("File opened")
            }
        }
    }

    override fun onOpenFiles(uris: List<Uri>) {
        coroutineScope.launch {
            documentFiles = mediaStore.openDocuments(uris).map { it.toViewData() }
            logger.log("Files opened")
        }
    }
}