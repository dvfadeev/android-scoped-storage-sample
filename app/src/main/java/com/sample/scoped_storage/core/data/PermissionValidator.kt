package com.sample.scoped_storage.core.data

import com.sample.scoped_storage.core.ui.widgets.DialogData

interface PermissionValidator {

    companion object {
        const val REQUEST_ID = 1
    }

    fun requestPermission(
        permission: String,
        onUpdateDialogData: (DialogData?) -> Unit,
        onGranted: (() -> Unit),
        onDenied: (() -> Unit)? = null
    )

    enum class Result {
        Granted, DeniedPermanently, Denied
    }
}