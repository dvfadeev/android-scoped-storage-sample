package com.sample.scoped_storage.modules.media_store.data

import android.app.Activity
import android.app.RecoverableSecurityException
import android.content.ContentUris
import android.content.ContentValues
import android.content.Context
import android.content.IntentSender
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.activity.result.IntentSenderRequest
import com.sample.scoped_storage.core.utils.*
import com.sample.scoped_storage.modules.media_store.domain.DetailedMediaFile
import com.sample.scoped_storage.modules.media_store.domain.MediaFile
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.IOException

/**
 * Interaction with media files is performed through the content resolver
 */
class MediaStoreGatewayImpl(
    private val context: Context,
    private val activityProvider: ActivityProvider
) : MediaStoreGateway {

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
                val type = cursor.getString(typeColumnIndex)?.split(File.separator)?.first()
                val sizeKb = cursor.getLong(sizeColumnIndex) / 1024
                val date = cursor.getLong(dateColumnIndex) * 1000

                if (name == null || type == null) {
                    // Ignore broken files
                    continue
                }

                val uriColumn = when (type) {
                    "image" -> MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                    "video" -> MediaStore.Video.Media.EXTERNAL_CONTENT_URI
                    "audio" -> MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
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

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                put(MediaStore.MediaColumns.IS_PENDING, 1)
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

                    values.put(MediaStore.MediaColumns.SIZE, bitmap.byteCount)

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        values.put(MediaStore.MediaColumns.IS_PENDING, 0)
                    }

                    update(it, values, null, null)
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

            val name = cursor.getStringFromColumn(MediaStore.Files.FileColumns.DISPLAY_NAME)
            val title = cursor.getNullableStringFromColumn(MediaStore.Files.FileColumns.TITLE)
            val path = cursor.getNullableStringFromColumn(pathColumn)
            val mimeType = cursor.getStringFromColumn(MediaStore.Files.FileColumns.MIME_TYPE)
            val sizeKb = cursor.getIntFromColumn(MediaStore.Files.FileColumns.SIZE) / 1024
            val dateAdded = cursor.getNullableLongFromColumn(MediaStore.Files.FileColumns.DATE_ADDED)?.let {
                it * 1000
            }

            // Image / Video fields
            val height: String?
            val width: String?
            val dateTaken: Long?
            val description: String?

            if (mimeType.startsWith(FileTypes.MIME_TYPE_IMAGE_ALL) || mimeType.startsWith(FileTypes.MIME_TYPE_VIDEO_ALL)) {
                height = cursor.getNullableStringFromColumn(MediaStore.Files.FileColumns.HEIGHT)
                width = cursor.getNullableStringFromColumn(MediaStore.Files.FileColumns.WIDTH)
                dateTaken = cursor.getNullableLongFromColumn(MediaStore.Images.ImageColumns.DATE_TAKEN)
                description = cursor.getNullableStringFromColumn(MediaStore.Images.ImageColumns.DESCRIPTION)
            } else {
                height = null
                width = null
                dateTaken = null
                description = null
            }

            // Video / Audio fields
            val duration = if (mimeType.startsWith(FileTypes.MIME_TYPE_VIDEO_ALL) || mimeType.startsWith(FileTypes.MIME_TYPE_AUDIO_ALL)) {
                cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.VideoColumns.DURATION))
            } else {
                null
            }

            resultImage = DetailedMediaFile(
                uri = uri,
                name = name,
                title = title,
                path = path,
                mimeType = mimeType,
                sizeKb = sizeKb,
                dateAdded = dateAdded,
                dateTaken = dateTaken,
                description = description,
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
     * On Android 10 and above, removing files that other apps own is restricted,
     * you need to ask for permission to delete a file
     * onFileRemoved - operation result
     */
    override suspend fun removeMediaFile(uri: Uri, onFileRemoved: (MediaStoreGateway.FileRemoveResult) -> Unit) =
        withContext(Dispatchers.IO) {
            val resolver = context.contentResolver
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                try {
                    resolver.delete(uri, null, null)
                    onFileRemoved(MediaStoreGateway.FileRemoveResult.Completed)
                } catch (e: SecurityException) {
                    when {
                        // API 30 createWriteRequest from MediaStore
                        Build.VERSION.SDK_INT >= Build.VERSION_CODES.R -> {
                            val uris = if (MediaStoreGateway.API_30_MULTIPLY_WRITE_PERMISSIONS_ENABLED) {
                                loadMediaFiles().map { it.uri }
                            } else {
                                listOf(uri)
                            }
                            val request = IntentSenderRequest.Builder(MediaStore.createWriteRequest(resolver, uris).intentSender).build()
                            try {
                                activityProvider.getActivity().getIntentSenderResult(request) { result ->
                                    when (result.resultCode) {
                                        Activity.RESULT_OK -> {
                                            resolver.delete(uri, null, null)
                                            onFileRemoved(MediaStoreGateway.FileRemoveResult.Completed)
                                        }
                                        Activity.RESULT_CANCELED -> {
                                            onFileRemoved(MediaStoreGateway.FileRemoveResult.PermissionDenied)
                                        }
                                    }
                                }
                            } catch (e: IntentSender.SendIntentException) {
                                onFileRemoved(MediaStoreGateway.FileRemoveResult.Error)
                            }
                        }

                        // API 29 createWriteRequest from RecoverableSecurityException
                        Build.VERSION.SDK_INT == Build.VERSION_CODES.Q -> {
                            val intentSender = (e as? RecoverableSecurityException)?.userAction?.actionIntent?.intentSender
                            intentSender?.let {
                                try {
                                    val request = IntentSenderRequest.Builder(it).build()
                                    activityProvider.getActivity().getIntentSenderResult(request) { result ->
                                        when (result.resultCode) {
                                            Activity.RESULT_OK -> {
                                                resolver.delete(uri, null, null)
                                                onFileRemoved(MediaStoreGateway.FileRemoveResult.Completed)
                                            }
                                            Activity.RESULT_CANCELED -> {
                                                onFileRemoved(MediaStoreGateway.FileRemoveResult.PermissionDenied)
                                            }
                                        }
                                    }
                                } catch (e: IntentSender.SendIntentException) {
                                    // Nothing
                                }
                            }
                        }
                        else -> {
                            onFileRemoved(MediaStoreGateway.FileRemoveResult.Error)
                        }
                    }
                }
            } else {
                resolver.delete(uri, null, null)
                onFileRemoved(MediaStoreGateway.FileRemoveResult.Completed)
            }
            return@withContext
        }
}