package com.example.scoped_storage_example.core.data

import kotlinx.datetime.Clock
import java.text.SimpleDateFormat
import java.util.*

class CurrentTimeImpl : CurrentTime {

    override val currentTimeString: String
        get() {
            val dateFormatter = SimpleDateFormat(FILE_DATE_PATTERN, Locale.getDefault())
            val date = Date(Clock.System.now().toEpochMilliseconds())
            return dateFormatter.format(date)
        }
}