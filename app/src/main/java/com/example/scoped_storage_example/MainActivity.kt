package com.example.scoped_storage_example

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import com.arkivanov.decompose.defaultComponentContext
import com.example.scoped_storage_example.core.koin
import com.example.scoped_storage_example.core.utils.ComponentFactory
import com.example.scoped_storage_example.root.createRootComponent
import com.example.scoped_storage_example.root.ui.RootComponent
import com.example.scoped_storage_example.root.ui.RootUi

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