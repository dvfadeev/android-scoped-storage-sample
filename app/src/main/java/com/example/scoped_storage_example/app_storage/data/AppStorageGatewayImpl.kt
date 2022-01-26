package com.example.scoped_storage_example.app_storage.data

import android.content.Context

class AppStorageGatewayImpl(private val context: Context) : AppStorageGateway {

    companion object {
        private const val TYPE_TEXT = ".txt"
    }

    override fun writeTextFile(fileName: String, content: String) {
        writeFile(fileName + TYPE_TEXT, content.toByteArray())
    }

    override fun writeFile(fileName: String, content: ByteArray) {
        context.openFileOutput(fileName, Context.MODE_PRIVATE).use {
            it.write(content)
        }
    }

    override fun openFile(fileName: String): String {
        var content = ""
        context.openFileInput(fileName).bufferedReader().useLines { lines ->
            content = lines.joinToString(separator = "\n")
        }
        return content
    }

    override fun removeFile(fileName: String) {
        context.deleteFile(fileName)
    }

    override fun getFilesList(): List<String> {
        return context.fileList().toList()
    }
}