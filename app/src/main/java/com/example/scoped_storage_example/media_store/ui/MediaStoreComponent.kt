package com.example.scoped_storage_example.media_store.ui

import android.graphics.Bitmap
import android.net.Uri
import com.example.scoped_storage_example.media_store.data.MediaType

interface MediaStoreComponent {

    var mediaType: MediaType

    var mediaFiles: List<MediaFileViewData>?

    var selectedMediaFIle: DetailedImageFileViewData?

    var isShowImageFileContent: Boolean

    fun onLoadMedia()

    fun onSaveBitmap(bitmap: Bitmap)

    fun onChangeMediaType(mediaType: MediaType)

    fun onFileClick(uri: Uri)

    fun onFileRemoveClick(uri: Uri)
}