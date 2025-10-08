package com.yong.taximeter.ui.main.subscreen.setting.dialog

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun CommonRadioListDialog(
    titleRes: StringResource? = null,
    title: String? = null,
    selectedIdx: Int,
    itemList: List<String>,
    onSelectItem: (idx: Int) -> Unit,
    onCancel: () -> Unit,
) {
    val dialogTitle = (if(titleRes != null) stringResource(titleRes) else title) ?: ""

    Dialog(
        onDismissRequest = onCancel,
    ) {
        Card(
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 8.dp
            )
        ) {
            Column(
                modifier = Modifier
                    .padding(18.dp)
            ) {
                Text(
                    text = dialogTitle,
                )

                LazyColumn(
                    modifier = Modifier
                        .padding(vertical = 8.dp)
                ) {
                    items(itemList.size) { idx ->
                        val itemText = itemList[idx]
                        val isSelected = (idx == selectedIdx)
                        val onClickItem = { onSelectItem(idx) }

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable(onClick = onClickItem)
                        ) {
                            RadioButton(
                                selected = isSelected,
                                onClick = onClickItem
                            )
                            Text(
                                text = itemText,
                                modifier = Modifier
                                    .padding(horizontal = 8.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}