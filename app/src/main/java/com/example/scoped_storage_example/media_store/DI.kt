package com.example.scoped_storage_example.media_store

import com.arkivanov.decompose.ComponentContext
import com.example.scoped_storage_example.core.utils.ComponentFactory
import com.example.scoped_storage_example.media_store.ui.MediaStoreComponent
import com.example.scoped_storage_example.media_store.ui.RealMediaStoreComponent
import org.koin.dsl.module

val mediaStoreModule = module {
}

fun ComponentFactory.createMediaStoreComponent(
    componentContext: ComponentContext
): MediaStoreComponent {
    return RealMediaStoreComponent(componentContext)
}