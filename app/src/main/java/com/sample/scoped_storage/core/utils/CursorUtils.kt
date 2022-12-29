package com.sample.scoped_storage.core.utils

import android.database.Cursor

fun Cursor.getStringFromColumn(column: String) = getString(getColumnIndexOrThrow(column))

fun Cursor.getIntFromColumn(column: String) = getInt(getColumnIndexOrThrow(column))

fun Cursor.getLongFromColumn(column: String) = getLong(getColumnIndexOrThrow(column))

fun Cursor.getNullableStringFromColumn(column: String) = getColumnIndex(column).run {
    if (this >= 0) {
        getString(this)
    } else {
        null
    }
}

fun Cursor.getNullableLongFromColumn(column: String) = getColumnIndex(column).run {
    if (this >= 0) {
        getLong(this)
    } else {
        null
    }
}