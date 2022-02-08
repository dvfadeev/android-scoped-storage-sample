package com.example.scoped_storage_example.core.data

const val FILE_DATE_PATTERN = "dd.MM.yyyy HH:mm:ss"

interface CurrentTime {

    val currentTimeString: String
}