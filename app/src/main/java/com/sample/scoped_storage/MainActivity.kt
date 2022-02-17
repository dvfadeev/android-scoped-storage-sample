package com.sample.scoped_storage

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import com.arkivanov.decompose.defaultComponentContext
import com.sample.scoped_storage.core.koin
import com.sample.scoped_storage.core.utils.ComponentFactory
import com.sample.scoped_storage.root.createRootComponent
import com.sample.scoped_storage.root.ui.RootComponent
import com.sample.scoped_storage.root.ui.RootUi

class MainActivity : AppCompatActivity() {

    private lateinit var rootComponent: RootComponent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val componentFactory = application.koin.get<ComponentFactory>()
        rootComponent = componentFactory.createRootComponent(defaultComponentContext())

        setContent {
            RootUi(rootComponent)
        }
    }
}