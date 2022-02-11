package com.example.scoped_storage_example.modules.media_store.ui

import com.example.scoped_storage_example.R

enum class PermissionRequest(val permission: String, val messageResource: Int) {
    ReadStorage(android.Manifest.permission.READ_EXTERNAL_STORAGE, R.string.media_store_read_permission_request),
    TakePhoto(android.Manifest.permission.WRITE_EXTERNAL_STORAGE, R.string.media_store_take_photo_permission_request),
    RemoveFile(android.Manifest.permission.WRITE_EXTERNAL_STORAGE, R.string.media_store_remove_file_permission_request)
}