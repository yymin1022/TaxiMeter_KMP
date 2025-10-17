package com.yong.taximeter.common.ui

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun ShowSnackBar(
    snackBarHostState: SnackbarHostState,
    messageRes: StringResource? = null,
    message: String? = null,
    dismissSnackBar: () -> Unit,
) {
    val snackBarText = messageRes?.let { stringResource(it) } ?: message

    LaunchedEffect(message, messageRes) {
        if(snackBarText != null) {
            snackBarHostState.showSnackbar(snackBarText)
            dismissSnackBar()
        }
    }
}