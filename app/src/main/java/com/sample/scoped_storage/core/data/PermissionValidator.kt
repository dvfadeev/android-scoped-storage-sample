package com.sample.scoped_storage.core.data

import com.sample.scoped_storage.core.ui.widgets.DialogData

interface PermissionValidator {

    companion object {
        const val REQUEST_ID = 1
    }

    fun validatePermission(
        permission: String,
        messageRes: Int,
        onUpdateDialogData: (DialogData?) -> Unit,
        onGranted: (() -> Unit),
        onDenied: (() -> Unit)? = null
    )

    suspend fun checkPermission(permission: String): Result

    suspend fun requestPermission(permission: String): Result

    enum class Result {
        Granted, DeniedPermanently, Denied
    }
}