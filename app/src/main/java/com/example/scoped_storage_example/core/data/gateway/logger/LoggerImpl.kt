package com.example.scoped_storage_example.core.data.gateway.logger

import com.example.scoped_storage_example.core.data.gateway.current_time.CurrentTime

class LoggerImpl(private val time: CurrentTime) : Logger {

    private val currentSession = StringBuilder()

    override fun log(string: String) {
        currentSession.appendLine(
            time.currentTimeString + ": " + string
        )
    }

    override fun getSession(): SessionLogs {
        return SessionLogs(
            time = time.currentTimeString,
            logs = currentSession.toString()
        )
    }
}