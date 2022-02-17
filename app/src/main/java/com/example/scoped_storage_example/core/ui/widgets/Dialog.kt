package com.example.scoped_storage_example.core.ui.widgets

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties
import com.example.scoped_storage_example.R

open class DialogData(
    val titleRes: Int,
    val messageRes: Int? = null,
    val onAcceptClick: (() -> Unit)? = null,
    val onCancelClick: (() -> Unit)? = null
)

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun Dialog(
    data: DialogData
) {
    AlertDialog(
        onDismissRequest = data.onCancelClick ?: data.onAcceptClick ?: { },
        properties = DialogProperties(usePlatformDefaultWidth = false),
        title = null,
        modifier = Modifier.padding(horizontal = 32.dp),
        text = {
            Column(
                modifier = Modifier.wrapContentWidth()
            ) {
                Text(
                    text = stringResource(id = data.titleRes),
                    color = MaterialTheme.colors.onBackground,
                    style = TextStyle(
                        fontWeight = FontWeight.Medium,
                        fontSize = 18.sp,
                        letterSpacing = 0.15.sp
                    ),
                    modifier = Modifier.padding(vertical = 16.dp)
                )

                data.messageRes?.let { messageRes ->
                    Text(
                        text = stringResource(id = messageRes)
                    )
                }

                if (data is EditTextDialogData) {
                    var value by remember { mutableStateOf(data.initText) }

                    OutlinedTextField(
                        value = value,
                        onValueChange = {
                            value = it
                            data.onTextChanged(it)
                        }
                    )
                }
            }
        },
        buttons = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp)
            ) {
                Spacer(modifier = Modifier.weight(1f))

                data.onCancelClick?.let { onClick ->
                    TextButton(
                        onClick = onClick
                    ) {
                        Text(text = stringResource(id = R.string.cancel))
                    }
                }

                data.onAcceptClick?.let { onClick ->
                    TextButton(
                        onClick = onClick
                    ) {
                        Text(text = stringResource(id = R.string.ok))
                    }
                }
            }
        }
    )
}