package com.example.scoped_storage_example.core.data

import android.os.Build
import kotlinx.datetime.Clock
import java.text.SimpleDateFormat
import java.util.*

class CurrentTimeImpl : CurrentTime {

    override val currentTimeString: String
        get() {
            val time = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                Clock.System.now().epochSeconds
            } else {
                System.currentTimeMillis()
            }

            val dateFormatter = SimpleDateFormat(FILE_DATE_PATTERN, Locale.getDefault())
            val date = Date(time)
            return dateFormatter.format(date)
        }
}