package com.sample.scoped_storage.core.utils

import android.os.Build
import android.os.ext.SdkExtensions.getExtensionVersion

private const val ANDROID_R_REQUIRED_EXTENSION_VERSION = 2

object PhotoPickerAvailabilityChecker {

    // PhotoPicker is available since Android TIRAMISU (API 33), moreover it has been backported on Android 11 & 12 (API 30+)
    // We also need to check for the SDK extension version because only if the required device SDK extension is available,
    // the Photo Picker is actually supported
    fun isPhotoPickerAvailable(): Boolean {
        return when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU -> true
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.R -> {
                getExtensionVersion(Build.VERSION_CODES.R) >= ANDROID_R_REQUIRED_EXTENSION_VERSION
            }
            else -> false
        }
    }
}