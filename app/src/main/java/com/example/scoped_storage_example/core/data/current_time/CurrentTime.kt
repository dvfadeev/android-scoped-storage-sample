package com.example.scoped_storage_example.core.data.current_time

import kotlinx.datetime.Instant

interface CurrentTime {

    val currentTime: Instant

    val currentTimeString: String
}