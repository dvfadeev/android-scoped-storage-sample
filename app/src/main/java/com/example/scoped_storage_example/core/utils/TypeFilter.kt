package com.example.scoped_storage_example.core.utils

import com.example.scoped_storage_example.R

enum class TypeFilter(val mime: String, val resource: Int) {
    All("*/*", R.string.filter_all),
    Images("image/*", R.string.filter_images),
    Audio("audio/*", R.string.filter_audio),
    Videos("video/*", R.string.filter_videos)
}

object AvailableFilters {

    val list = listOf(
        TypeFilter.All, TypeFilter.Images, TypeFilter.Audio, TypeFilter.Videos
    )
}