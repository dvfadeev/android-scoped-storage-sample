package com.example.scoped_storage_example.core.data

import kotlinx.datetime.Instant

const val FILE_DATE_PATTERN = "dd.MM.yyyy HH:mm:ss"

interface CurrentTime {

    val currentTime: Instant

    val currentTimeString: String
}