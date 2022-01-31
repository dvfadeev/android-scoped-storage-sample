package com.example.scoped_storage_example.media_store.data

import android.content.ContentUris
import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import com.example.scoped_storage_example.core.data.gateway.FileTypes
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.IOException

class MediaStoreGatewayImpl(private val context: Context) : MediaStoreGateway {

    override suspend fun loadMediaFiles(
        mediaType: MediaType
    ): List<MediaFile> = withContext(Dispatchers.IO) {
        val files = mutableListOf<MediaFile>()
        val resolver = context.contentResolver
        val projection = arrayOf(
            MediaStore.Files.FileColumns._ID,
            MediaStore.Files.FileColumns.DISPLAY_NAME,
            MediaStore.Files.FileColumns.MIME_TYPE,
            MediaStore.Files.FileColumns.SIZE,
            MediaStore.Files.FileColumns.DATE_ADDED
        )

        val contentUri = when (mediaType) {
            MediaType.All -> MediaStore.Files.getContentUri("external")
            MediaType.Images -> MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            MediaType.Videos -> MediaStore.Video.Media.EXTERNAL_CONTENT_URI
            MediaType.Audio -> MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
        }

        resolver.query(
            contentUri,
            projection,
            null,
            null,
            null
        )?.use { cursor ->
            val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns._ID)
            val nameColumn = cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.DISPLAY_NAME)
            val typeColumn = cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.MIME_TYPE)
            val sizeColumn = cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.SIZE)
            val dateColumn = cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.DATE_ADDED)

            while (cursor.moveToNext()) {
                val id = cursor.getLong(idColumn)
                val name = cursor.getString(nameColumn)
                val type = cursor.getString(typeColumn).split(File.separator).first()
                val sizeKb = cursor.getInt(sizeColumn) / 1024
                val date = cursor.getString(dateColumn)

                val uriColumn = when (type) {
                    "image" -> MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                    "video" -> MediaStore.Video.Media.EXTERNAL_CONTENT_URI
                    else -> null
                }

                val uri = uriColumn?.let {
                    ContentUris.withAppendedId(it, id)
                }

                files += MediaFile(
                    uri = uri,
                    name = name,
                    type = type,
                    sizeKb = sizeKb,
                    date = date.toLong() * 1000
                )
            }
        }
        return@withContext files.reversed()
    }

    override suspend fun savePhoto(fileName: String, bitmap: Bitmap) = withContext(Dispatchers.IO) {
        val values = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, fileName + "." + FileTypes.TYPE_PHOTO)
            put(MediaStore.MediaColumns.MIME_TYPE, FileTypes.MIME_TYPE_PHOTO)

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
            } else {
                put(MediaStore.Images.Media.DATA, Environment.DIRECTORY_PICTURES)
            }
        }

        var uri: Uri? = null

        runCatching {
            with(context.contentResolver) {
                insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)?.also {
                    uri = it
                    openOutputStream(it)?.use { stream ->
                        if (!bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)) {
                            throw IOException("Failed to save bitmap!")
                        }
                    } ?: throw IOException("Failed to create new MediaStore record!")
                }
            }
        }.getOrElse {
            uri?.let { orphanUri ->
                context.contentResolver.delete(orphanUri, null, null)
            }
            throw it
        }
        return@withContext
    }

    override suspend fun removeMediaFile(uri: Uri) = withContext(Dispatchers.IO) {
        val resolver = context.contentResolver
        resolver.delete(uri, null, null)
        return@withContext
    }
}