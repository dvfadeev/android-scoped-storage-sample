package com.sample.scoped_storage.core.data

import android.content.Context
import android.widget.Toast

class ComponentToastImpl(private val context: Context) : ComponentToast {

    override fun show(string: String, isLong: Boolean) {
        val length = if (isLong) Toast.LENGTH_LONG else Toast.LENGTH_SHORT
        Toast.makeText(context, string, length).show()
    }

    override fun show(resource: Int, isLong: Boolean) {
        show(context.getString(resource), isLong)
    }
}