package com.example.scoped_storage_example.file_picker

import com.arkivanov.decompose.ComponentContext
import com.example.scoped_storage_example.core.utils.ComponentFactory
import com.example.scoped_storage_example.file_picker.data.FilePickerGateway
import com.example.scoped_storage_example.file_picker.data.FilePickerGatewayImpl
import com.example.scoped_storage_example.file_picker.ui.FilePickerComponent
import com.example.scoped_storage_example.file_picker.ui.RealFilePickerComponent
import org.koin.android.ext.koin.androidContext
import org.koin.core.component.get
import org.koin.dsl.module

val filePickerModule = module {
    single<FilePickerGateway> { FilePickerGatewayImpl(androidContext()) }
}

fun ComponentFactory.createFilePickerComponent(
    componentContext: ComponentContext
): FilePickerComponent {
    return RealFilePickerComponent(componentContext, get(), get())
}