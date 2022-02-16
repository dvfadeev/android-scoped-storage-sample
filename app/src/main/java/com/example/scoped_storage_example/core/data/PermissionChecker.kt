package com.example.scoped_storage_example.core.data

interface PermissionChecker {

    suspend fun checkPermission(permission: String): Result

    enum class Result {
        Granted, Denied, ShowMessage
    }
}