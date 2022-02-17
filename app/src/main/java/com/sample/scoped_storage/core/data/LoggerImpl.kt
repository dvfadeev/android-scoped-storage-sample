package com.sample.scoped_storage.core.data

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