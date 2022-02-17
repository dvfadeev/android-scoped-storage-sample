package com.sample.scoped_storage.modules.file_picker

import com.arkivanov.decompose.ComponentContext
import com.sample.scoped_storage.core.utils.ComponentFactory
import com.sample.scoped_storage.modules.file_picker.data.FilePickerGateway
import com.sample.scoped_storage.modules.file_picker.data.FilePickerGatewayImpl
import com.sample.scoped_storage.modules.file_picker.ui.FilePickerComponent
import com.sample.scoped_storage.modules.file_picker.ui.RealFilePickerComponent
import org.koin.android.ext.koin.androidContext
import org.koin.core.component.get
import org.koin.dsl.module

val filePickerModule = module {
    single<FilePickerGateway> { FilePickerGatewayImpl(androidContext()) }
}

fun ComponentFactory.createFilePickerComponent(
    componentContext: ComponentContext
): FilePickerComponent {
    return RealFilePickerComponent(componentContext, get(), get(), get())
}