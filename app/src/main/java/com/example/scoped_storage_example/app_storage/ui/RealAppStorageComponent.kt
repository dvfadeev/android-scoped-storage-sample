package com.example.scoped_storage_example.app_storage.ui

import com.arkivanov.decompose.ComponentContext

class RealAppStorageComponent(
    componentContext: ComponentContext
) : ComponentContext by componentContext, AppStorageComponent