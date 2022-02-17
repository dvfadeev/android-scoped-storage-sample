package com.sample.scoped_storage.core.data

interface ComponentToast {

    fun show(string: String, isLong: Boolean = false)

    fun show(resource: Int, isLong: Boolean = false)
}