package com.sample.scoped_storage.modules.media_store

import com.arkivanov.decompose.ComponentContext
import com.sample.scoped_storage.core.utils.ComponentFactory
import com.sample.scoped_storage.modules.media_store.data.MediaStoreGateway
import com.sample.scoped_storage.modules.media_store.data.MediaStoreGatewayImpl
import com.sample.scoped_storage.modules.media_store.ui.MediaStoreComponent
import com.sample.scoped_storage.modules.media_store.ui.RealMediaStoreComponent
import org.koin.android.ext.koin.androidContext
import org.koin.core.component.get
import org.koin.dsl.module

val mediaStoreModule = module {
    single<MediaStoreGateway> { MediaStoreGatewayImpl(androidContext(), get()) }
}

fun ComponentFactory.createMediaStoreComponent(
    componentContext: ComponentContext,
    onOutput: (MediaStoreComponent.Output) -> Unit,
): MediaStoreComponent {
    return RealMediaStoreComponent(componentContext, onOutput, get(), get(), get(), get(), get())
}