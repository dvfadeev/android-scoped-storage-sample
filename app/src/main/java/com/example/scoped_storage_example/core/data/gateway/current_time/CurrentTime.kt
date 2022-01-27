package com.example.scoped_storage_example.core.data.gateway.current_time

import kotlinx.datetime.Instant

interface CurrentTime {

    val currentTime: Instant

    val currentTimeString: String
}