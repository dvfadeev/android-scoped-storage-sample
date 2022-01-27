package com.example.scoped_storage_example.media_store.ui

import com.arkivanov.decompose.ComponentContext

class RealMediaStoreComponent(
    componentContext: ComponentContext
) : ComponentContext by componentContext, MediaStoreComponent