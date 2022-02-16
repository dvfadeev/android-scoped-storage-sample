package com.example.scoped_storage_example.core.data

import android.content.Context
import com.eazypermissions.common.model.PermissionResult
import com.eazypermissions.coroutinespermission.PermissionManager
import com.example.scoped_storage_example.App
import com.example.scoped_storage_example.MainActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class PermissionCheckerImpl(private val context: Context) : PermissionChecker {

    override suspend fun checkPermission(permission: String): PermissionChecker.Result = withContext(Dispatchers.Main) {

        val permissionResult = PermissionManager.requestPermissions(
            (context as App).activeActivity as MainActivity,
            1,
            permission
        )

        return@withContext when (permissionResult) {
            is PermissionResult.PermissionGranted -> {
                PermissionChecker.Result.Granted
            }
            is PermissionResult.PermissionDenied -> {
                PermissionChecker.Result.Denied
            }
            is PermissionResult.PermissionDeniedPermanently -> {
                PermissionChecker.Result.Denied
            }
            is PermissionResult.ShowRational -> {
                PermissionChecker.Result.ShowMessage
            }
        }
    }
}