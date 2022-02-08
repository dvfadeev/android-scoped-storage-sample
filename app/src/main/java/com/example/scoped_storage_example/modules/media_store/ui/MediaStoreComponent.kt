package com.example.scoped_storage_example.modules.media_store.ui

import android.graphics.Bitmap
import android.net.Uri
import com.example.scoped_storage_example.core.utils.TypeFilter

interface MediaStoreComponent {

    var filter: TypeFilter

    var mediaFiles: List<MediaFileViewData>?

    var selectedMediaFIle: DetailedImageFileViewData?

    var isShowImageFileContent: Boolean

    fun onLoadMedia()

    fun onSaveBitmap(bitmap: Bitmap)

    fun onChangeFilter(filter: TypeFilter)

    fun onFileClick(uri: Uri)

    fun onFileRemoveClick(uri: Uri)
}