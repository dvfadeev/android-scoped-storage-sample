package com.sample.scoped_storage.core.ui.widgets

class EditTextDialogData(
    val initText: String,
    val onTextChanged: (String) -> Unit,
    titleRes: Int,
    onAcceptClick: (() -> Unit)? = null,
    onCancelClick: (() -> Unit)? = null
) : DialogData(titleRes, null, onAcceptClick, onCancelClick)