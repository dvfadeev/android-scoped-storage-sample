package com.example.scoped_storage_example.modules.media_store.ui

import android.graphics.Bitmap
import android.net.Uri
import com.example.scoped_storage_example.core.utils.TypeFilter

interface MediaStoreComponent {

    val filter: TypeFilter

    val mediaFiles: List<MediaFileViewData>?

    val permissionRequest: PermissionRequest

    val selectedUri: Uri?

    val selectedMediaFile: DetailedImageFileViewData?

    val isShowImageFileContent: Boolean

    fun onLoadMedia()

    fun onRequestPermission(permissionRequest: PermissionRequest)

    fun onSaveBitmap(bitmap: Bitmap)

    fun onChangeFilter(filter: TypeFilter)

    fun onFileClick(uri: Uri)

    fun onFileLongClick(uri: Uri)

    fun onFileRemoveClick(uri: Uri)
}