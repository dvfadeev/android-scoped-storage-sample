package com.sample.scoped_storage

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.arkivanov.decompose.defaultComponentContext
import com.sample.scoped_storage.core.koin
import com.sample.scoped_storage.core.utils.ComponentFactory
import com.sample.scoped_storage.root.createRootComponent
import com.sample.scoped_storage.root.ui.RootComponent
import com.sample.scoped_storage.root.ui.RootUi

class MainActivity : AppCompatActivity() {

    private lateinit var rootComponent: RootComponent

    private var onActivityResult: ((ActivityResult) -> Unit)? = null

    private val intentSenderResult = registerForActivityResult(ActivityResultContracts.StartIntentSenderForResult()) {
        onActivityResult?.invoke(it)
        onActivityResult = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val componentFactory = application.koin.get<ComponentFactory>()
        rootComponent = componentFactory.createRootComponent(defaultComponentContext())

        setContent {
            RootUi(rootComponent)
        }
    }

    fun getIntentSenderResult(intentSenderRequest: IntentSenderRequest, onActivityResult: (ActivityResult) -> Unit) {
        this.onActivityResult = onActivityResult
        intentSenderResult.launch(intentSenderRequest)
    }
}