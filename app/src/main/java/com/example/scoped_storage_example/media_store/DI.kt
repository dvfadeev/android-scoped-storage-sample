package com.example.scoped_storage_example.media_store

import com.arkivanov.decompose.ComponentContext
import com.example.scoped_storage_example.core.utils.ComponentFactory
import com.example.scoped_storage_example.media_store.data.MediaStoreGateway
import com.example.scoped_storage_example.media_store.data.MediaStoreGatewayImpl
import com.example.scoped_storage_example.media_store.ui.MediaStoreComponent
import com.example.scoped_storage_example.media_store.ui.RealMediaStoreComponent
import org.koin.android.ext.koin.androidContext
import org.koin.core.component.get
import org.koin.dsl.module

val mediaStoreModule = module {
    single<MediaStoreGateway> { MediaStoreGatewayImpl(androidContext()) }
}

fun ComponentFactory.createMediaStoreComponent(
    componentContext: ComponentContext
): MediaStoreComponent {
    return RealMediaStoreComponent(componentContext, get(), get(), get())
}