package com.example.scoped_storage_example.core.data

interface ComponentToast {

    fun show(string: String, isLong: Boolean = false)

    fun show(resource: Int, isLong: Boolean = false)
}