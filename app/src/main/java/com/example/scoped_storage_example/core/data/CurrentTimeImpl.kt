package com.example.scoped_storage_example.core.data

import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import java.text.SimpleDateFormat
import java.util.*

private const val DATE_PATTERN = "dd.MM.yyyy HH:mm:ss"

class CurrentTimeImpl : CurrentTime {

    override val currentTime: Instant
        get() = Clock.System.now()

    override val currentTimeString: String
        get() {
            val dateFormatter = SimpleDateFormat(DATE_PATTERN, Locale.getDefault())
            val date = Date(currentTime.toEpochMilliseconds())
            return dateFormatter.format(date)
        }
}