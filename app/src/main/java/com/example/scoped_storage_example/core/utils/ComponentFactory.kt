package com.example.scoped_storage_example.core.utils

import org.koin.core.Koin
import org.koin.core.component.KoinComponent

class ComponentFactory(private val localKoin: Koin) : KoinComponent {

    override fun getKoin(): Koin = localKoin
}