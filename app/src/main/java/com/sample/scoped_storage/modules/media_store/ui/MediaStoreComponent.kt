package com.sample.scoped_storage.modules.media_store.ui

import android.graphics.Bitmap
import android.net.Uri
import com.sample.scoped_storage.core.ui.widgets.DialogData
import com.sample.scoped_storage.core.utils.TypeFilter

interface MediaStoreComponent {

    val dialogData: DialogData?

    val isRefreshing: Boolean

    val filter: TypeFilter

    val mediaFiles: List<MediaFileViewData>?

    val selectedUri: Uri?

    val selectedMediaFile: DetailedImageFileViewData?

    val isShowImageFileContent: Boolean

    fun onLoadMedia()

    fun onSaveBitmap(bitmap: Bitmap)

    fun onChangeFilter(filter: TypeFilter)

    fun onFileClick(uri: Uri)

    fun onFileLongClick(uri: Uri)

    fun onFileRemoveClick(uri: Uri)

    sealed interface Output {
        object NavigationRequested : Output
    }
}