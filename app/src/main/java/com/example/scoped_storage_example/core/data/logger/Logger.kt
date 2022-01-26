package com.example.scoped_storage_example.core.data.logger

interface Logger {

    fun log(string: String)

    fun getSession(): SessionLogs
}

class SessionLogs(
    val time: String,
    val logs: String
)