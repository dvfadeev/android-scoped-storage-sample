package com.sample.scoped_storage.modules.file_picker.data

import android.net.Uri
import com.sample.scoped_storage.modules.file_picker.domain.DocumentFile

interface FilePickerGateway {

    suspend fun openDocument(uri: Uri): DocumentFile?

    suspend fun openDocuments(uris: List<Uri>): List<DocumentFile>

    suspend fun renameDocument(uri: Uri, documentName: String): Boolean

    suspend fun removeDocument(uri: Uri): Boolean
}