package com.yong.taximeter.common.ui.dialog

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource
import taximeter.composeapp.generated.resources.Res
import taximeter.composeapp.generated.resources.common_dialog_cancel
import taximeter.composeapp.generated.resources.common_dialog_confirm

@Composable
fun BasicDialog(
    titleRes: StringResource? = null,
    title: String? = null,
    descriptionRes: StringResource? = null,
    description: String? = null,
    onConfirm: (() -> Unit)? = null,
    onCancel: (() -> Unit)? = null,
) {
    val dialogTitle = if(titleRes != null) stringResource(titleRes) else title ?: ""
    val dialogDescription = if(descriptionRes != null) stringResource(descriptionRes) else description ?: ""

    AlertDialog(
        title = {
            Text(
                text = dialogTitle
            )
        },
        text = {
            Text(
                text = dialogDescription
            )
        },
        confirmButton = {
            TextButton(
                onClick = { onConfirm?.invoke() }
            ) {
                Text(stringResource(Res.string.common_dialog_confirm))
            }
        },
        dismissButton = {
            TextButton(
                onClick = { onCancel?.invoke() }
            ) {
                Text(stringResource(Res.string.common_dialog_cancel))
            }
        },
        onDismissRequest = { onCancel?.invoke() },
    )
}