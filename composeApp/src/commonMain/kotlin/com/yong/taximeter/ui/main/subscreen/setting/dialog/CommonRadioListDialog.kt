package com.yong.taximeter.ui.main.subscreen.setting.dialog

import androidx.compose.runtime.Composable
import androidx.compose.ui.window.Dialog

@Composable
fun CommonRadioListDialog(
    itemList: List<String>,
    onSelectItem: (idx: Int) -> Unit,
    onCancel: () -> Unit,
) {
    Dialog(
        onDismissRequest = onCancel,
    ) {

    }
}