package com.example.scoped_storage_example.media_store.data

import android.content.ContentUris
import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import com.example.scoped_storage_example.core.data.FileTypes
import com.example.scoped_storage_example.core.utils.TypeFilter
import com.example.scoped_storage_example.media_store.data.models.DetailedMediaFile
import com.example.scoped_storage_example.media_store.data.models.MediaFile
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.IOException

/**
 * Interaction with media files is performed through the content resolver
 */
class MediaStoreGatewayImpl(private val context: Context) : MediaStoreGateway {

    /**
     * Load all media files according to the selected type
     * @return list of MediaFile models
     */
    override suspend fun loadMediaFiles(
        filter: TypeFilter
    ): List<MediaFile> = withContext(Dispatchers.IO) {
        val resolver = context.contentResolver
        val files = mutableListOf<MediaFile>()

        val projection = arrayOf( // To save resources, we select only the required fields
            MediaStore.Files.FileColumns._ID,
            MediaStore.Files.FileColumns.DISPLAY_NAME,
            MediaStore.Files.FileColumns.MIME_TYPE,
            MediaStore.Files.FileColumns.SIZE,
            MediaStore.Files.FileColumns.DATE_ADDED
        )

        val uriContent = when (filter) {
            TypeFilter.All -> MediaStore.Files.getContentUri("external")
            TypeFilter.Images -> MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            TypeFilter.Videos -> MediaStore.Video.Media.EXTERNAL_CONTENT_URI
            TypeFilter.Audio -> MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
        }

        resolver.query(
            uriContent,
            projection,
            null,
            null,
            null
        )?.use { cursor ->
            val idColumnIndex = cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns._ID)
            val nameColumnIndex = cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.DISPLAY_NAME)
            val typeColumnIndex = cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.MIME_TYPE)
            val sizeColumnIndex = cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.SIZE)
            val dateColumnIndex = cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.DATE_ADDED)

            while (cursor.moveToNext()) {
                val id = cursor.getLong(idColumnIndex)
                val name = cursor.getString(nameColumnIndex)
                val type = cursor.getString(typeColumnIndex).split(File.separator).first()
                val sizeKb = cursor.getLong(sizeColumnIndex) / 1024
                val date = cursor.getLong(dateColumnIndex) * 1000

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
                    date = date
                )
            }
        }
        return@withContext files.reversed()
    }

    /**
     * Write image in media storage
     * The file will be saved in the pictures directory
     */
    override suspend fun writeImage(fileName: String, bitmap: Bitmap) = withContext(Dispatchers.IO) {
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

    /**
     * Open media file by uri
     * @return detailed MediaFile model with additional fields
     */
    override suspend fun openMediaFile(uri: Uri): DetailedMediaFile? = withContext(Dispatchers.IO) {
        val resolver = context.contentResolver
        var resultImage: DetailedMediaFile? = null

        val pathColumn = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            MediaStore.Files.FileColumns.RELATIVE_PATH
        } else {
            MediaStore.Files.FileColumns.DATA
        }

        resolver.query(
            uri,
            null, // All fields
            null,
            null,
            null
        )?.use { cursor ->
            cursor.moveToFirst()

            val name = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.DISPLAY_NAME))
            val title = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.TITLE))
            val path = cursor.getString(cursor.getColumnIndexOrThrow(pathColumn))
            val mimeType = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.MIME_TYPE))
            val sizeKb = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.SIZE)) / 1024
            val dateAdded = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.DATE_ADDED)) * 1000
            val height = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.HEIGHT))
            val width = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.WIDTH))
            val dateTaken = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Images.ImageColumns.DATE_TAKEN))
            val description = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.ImageColumns.DESCRIPTION))
            val duration = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.VideoColumns.DURATION))

            resultImage = DetailedMediaFile(
                uri = uri,
                name = name,
                title = title ?: "",
                path = path ?: "unknown",
                mimeType = mimeType ?: "unknown",
                sizeKb = sizeKb,
                dateAdded = dateAdded,
                dateTaken = dateTaken,
                description = description ?: "empty",
                height = height,
                width = width,
                duration = if (duration != null) {
                    duration.toLong() / 1000
                } else {
                    null
                }
            )
        }
        return@withContext resultImage
    }

    /**
     * Remove file from media storage
     * It will be removed from disk as well
     */
    override suspend fun removeMediaFile(uri: Uri) = withContext(Dispatchers.IO) {
        val resolver = context.contentResolver
        resolver.delete(uri, null, null)
        return@withContext
    }
}