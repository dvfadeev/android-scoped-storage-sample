package com.sample.scoped_storage.core.data

import com.eazypermissions.common.model.PermissionResult
import com.eazypermissions.coroutinespermission.PermissionManager
import com.sample.scoped_storage.R
import com.sample.scoped_storage.core.ui.widgets.DialogData
import com.sample.scoped_storage.core.utils.ActivityProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PermissionValidatorImpl(
    private val activityProvider: ActivityProvider,
    private val scope: CoroutineScope
) : PermissionValidator {

    /**
     * Request permission method
     * [DialogData] - dialog data to be displayed in component
     */
    override fun requestPermission(
        permission: String,
        onUpdateDialogData: (DialogData?) -> Unit,
        onGranted: (() -> Unit),
        onDenied: (() -> Unit)?
    ) {
        val permissionDeniedDialogData = DialogData(
            titleRes = R.string.media_store_permission_denied_title,
            messageRes = R.string.media_store_permission_denied,
            onAcceptClick = {
                onUpdateDialogData(null)
                onDenied?.invoke()
            }
        )

        scope.launch {
            when (processPermission(permission)) {
                PermissionValidator.Result.Granted -> {
                    onUpdateDialogData(null)
                    onGranted.invoke()
                }
                PermissionValidator.Result.Denied -> {
                    onUpdateDialogData(permissionDeniedDialogData)
                }
                PermissionValidator.Result.DeniedPermanently -> {
                    onUpdateDialogData(permissionDeniedDialogData)
                }
            }
        }
    }

    private suspend fun processPermission(permission: String): PermissionValidator.Result = withContext(Dispatchers.Main) {
        val permissionResult = PermissionManager.requestPermissions(
            activityProvider.getActivity(),
            PermissionValidator.REQUEST_ID,
            permission
        )

        return@withContext when (permissionResult) {
            is PermissionResult.PermissionGranted -> {
                PermissionValidator.Result.Granted
            }
            is PermissionResult.PermissionDenied -> {
                PermissionValidator.Result.Denied
            }
            is PermissionResult.PermissionDeniedPermanently -> {
                PermissionValidator.Result.DeniedPermanently
            }
            is PermissionResult.ShowRational -> {
                processPermission(permission)
            }
        }
    }
}