package com.example.scoped_storage_example.core.data

import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat.checkSelfPermission
import com.eazypermissions.common.model.PermissionResult
import com.eazypermissions.coroutinespermission.PermissionManager
import com.example.scoped_storage_example.R
import com.example.scoped_storage_example.core.ui.widgets.DialogData
import com.example.scoped_storage_example.core.utils.ActivityProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PermissionValidatorImpl(
    private val activityProvider: ActivityProvider,
    private val scope: CoroutineScope
) : PermissionValidator {

    /**
     * This method controls permission validation
     * [DialogData] - dialog data to be displayed in component
     */
    override fun validatePermission(
        permission: String,
        messageRes: Int,
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
            when (checkPermission(permission)) {
                PermissionValidator.Result.Granted -> {
                    onUpdateDialogData(null)
                    onGranted.invoke()
                }
                PermissionValidator.Result.DeniedPermanently -> {
                    onUpdateDialogData(permissionDeniedDialogData)
                }

                PermissionValidator.Result.Denied -> {
                    onUpdateDialogData(DialogData(
                        titleRes = R.string.media_store_permission_request_title,
                        messageRes = messageRes,
                        onAcceptClick = {
                            scope.launch {
                                when (requestPermission(permission)) {
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
                        },
                        onCancelClick = {
                            onUpdateDialogData(null)
                            onDenied?.invoke()
                        }
                    ))
                }
            }
        }
    }

    override suspend fun checkPermission(permission: String): PermissionValidator.Result {
        return if (checkSelfPermission(activityProvider.getActivity(), permission) == PackageManager.PERMISSION_GRANTED) {
            PermissionValidator.Result.Granted
        } else {
            if (ActivityCompat.shouldShowRequestPermissionRationale(activityProvider.getActivity(), permission)) {
                PermissionValidator.Result.Denied
            } else {
                PermissionValidator.Result.DeniedPermanently
            }
        }
    }

    override suspend fun requestPermission(permission: String): PermissionValidator.Result = withContext(Dispatchers.Main) {
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
                requestPermission(permission)
            }
        }
    }
}