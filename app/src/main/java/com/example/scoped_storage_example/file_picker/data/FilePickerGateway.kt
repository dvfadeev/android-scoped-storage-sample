package com.example.scoped_storage_example.file_picker.data

import android.net.Uri
import com.example.scoped_storage_example.file_picker.data.models.DocumentFile

interface FilePickerGateway {

    suspend fun openDocument(uri: Uri): DocumentFile?

    suspend fun openDocuments(uris: List<Uri>): List<DocumentFile>
}