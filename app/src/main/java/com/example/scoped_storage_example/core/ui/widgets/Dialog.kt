package com.example.scoped_storage_example.core.ui.widgets

import androidx.compose.foundation.layout.*
import androidx.compose.material.AlertDialog
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.scoped_storage_example.R

class DialogData(
    val titleRes: Int,
    val messageRes: Int? = null,
    val onAcceptClick: (() -> Unit)? = null,
    val onCancelClick: (() -> Unit)? = null
)

@Composable
fun Dialog(
    data: DialogData
) {
    AlertDialog(
        onDismissRequest = data.onCancelClick ?: data.onAcceptClick ?: { },
        title = null,
        text = {
            Column {
                Text(
                    text = stringResource(id = data.titleRes),
                    color = MaterialTheme.colors.onBackground,
                    style = TextStyle(
                        fontWeight = FontWeight.Medium,
                        fontSize = 18.sp,
                        letterSpacing = 0.15.sp
                    ),
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                data.messageRes?.let { messageRes ->
                    Text(
                        text = stringResource(id = messageRes)
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
                        Text(text = stringResource(id = R.string.file_picker_file_rename_cancel))
                    }
                }

                data.onAcceptClick?.let { onClick ->
                    TextButton(
                        onClick = onClick
                    ) {
                        Text(text = stringResource(id = R.string.file_picker_file_rename_accept))
                    }
                }
            }
        }
    )
}