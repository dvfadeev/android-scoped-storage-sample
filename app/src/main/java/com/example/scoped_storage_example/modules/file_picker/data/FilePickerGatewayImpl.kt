package com.example.scoped_storage_example.modules.file_picker.data

import android.content.Context
import android.net.Uri
import android.provider.DocumentsContract
import com.example.scoped_storage_example.modules.file_picker.data.models.DocumentFile
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.FileNotFoundException

/**
 * File picker only returns document uri,
 * To get the fields of a file you have to use content resolver
 */
class FilePickerGatewayImpl(private val context: Context) : FilePickerGateway {

    /**
     * Open document file by uri
     * @return DocumentFile
     */
    override suspend fun openDocument(uri: Uri): DocumentFile? = withContext(Dispatchers.IO) {
        val resolver = context.contentResolver
        var resultImage: DocumentFile? = null

        val projection = arrayOf( // Document columns
            DocumentsContract.Document.COLUMN_DISPLAY_NAME,
            DocumentsContract.Document.COLUMN_DOCUMENT_ID,
            DocumentsContract.Document.COLUMN_FLAGS,
            DocumentsContract.Document.COLUMN_MIME_TYPE,
            DocumentsContract.Document.COLUMN_SIZE,
            DocumentsContract.Document.COLUMN_LAST_MODIFIED,
            DocumentsContract.Document.COLUMN_ICON,
            DocumentsContract.Document.COLUMN_SUMMARY
        )

        resolver.query(
            uri,
            projection,
            null,
            null,
            null
        )?.use { cursor ->
            cursor.moveToFirst()

            val name = cursor.getString(cursor.getColumnIndexOrThrow(DocumentsContract.Document.COLUMN_DISPLAY_NAME))
            val id = cursor.getString(cursor.getColumnIndexOrThrow(DocumentsContract.Document.COLUMN_DOCUMENT_ID))
            val flags = cursor.getInt(cursor.getColumnIndexOrThrow(DocumentsContract.Document.COLUMN_FLAGS))
            val mimeType = cursor.getString(cursor.getColumnIndexOrThrow(DocumentsContract.Document.COLUMN_MIME_TYPE))
            val sizeKb = cursor.getLong(cursor.getColumnIndexOrThrow(DocumentsContract.Document.COLUMN_SIZE)) / 1024
            val dateModified = cursor.getLong(cursor.getColumnIndexOrThrow(DocumentsContract.Document.COLUMN_LAST_MODIFIED))
            val icon = cursor.getInt(cursor.getColumnIndexOrThrow(DocumentsContract.Document.COLUMN_ICON))
            val summary = cursor.getString(cursor.getColumnIndexOrThrow(DocumentsContract.Document.COLUMN_SUMMARY))

            resultImage = DocumentFile(
                uri = uri,
                name = name,
                id = id,
                flags = flags,
                mimeType = mimeType,
                sizeKb = sizeKb,
                dateModified = dateModified,
                icon = icon,
                summary = summary
            )
        }

        return@withContext resultImage
    }

    override suspend fun openDocuments(uris: List<Uri>): List<DocumentFile> {
        val result = mutableListOf<DocumentFile>()

        uris.forEach {
            openDocument(it)?.let { file ->
                result.add(file)
            }
        }

        return result
    }

    /**
     * Remove document file by uri
     * @return operation result
     */
    override suspend fun removeDocument(uri: Uri): Boolean = withContext(Dispatchers.IO) {
        val resolver = context.contentResolver

        resolver.query(
            uri,
            arrayOf(DocumentsContract.Document.COLUMN_FLAGS),
            null,
            null,
            null
        )?.use { cursor ->
            cursor.moveToFirst()
            val flags = cursor.getInt(cursor.getColumnIndexOrThrow(DocumentsContract.Document.COLUMN_FLAGS))
            val isSupportsDelete =
                flags and DocumentsContract.Document.FLAG_SUPPORTS_DELETE == DocumentsContract.Document.FLAG_SUPPORTS_DELETE

            if (isSupportsDelete) {
                try {
                    DocumentsContract.deleteDocument(resolver, uri)
                    return@withContext true
                } catch (e: FileNotFoundException) {
                    return@withContext false
                }
            }
        }
        return@withContext false
    }
}