package az.zero.azkeepit.ui.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.AlertDialog
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import az.zero.azkeepit.R

@Composable
fun DeleteDialog(
    openDialog: Boolean,
    text: String,
    onDismiss: () -> Unit,
    onDeleteClick: () -> Unit,
) {
    TextDialogWithTwoButtons(
        titleText = text,
        openDialog = openDialog,
        startBtnText = stringResource(id = R.string.delete),
        onStartBtnClick = onDeleteClick,
        startBtnStyle = MaterialTheme.typography.h3.copy(color = Color.Red),
        endBtnText = stringResource(id = R.string.cancel),
        onDismiss = onDismiss
    )
}

@Composable
fun EtDialogWithTwoButtons(
    text: String,
    headerText: String,
    hint: String = "",
    openDialog: Boolean,
    startBtnText: String,
    endBtnText: String,
    textStyle: TextStyle = MaterialTheme.typography.h2.copy(color = MaterialTheme.colors.onBackground),
    onTextChange: (String) -> Unit,
    isError: Boolean = false,
    errorText: String? = null,
    startBtnEnabled: Boolean = true,
    dismissAfterClickStartBtn: Boolean = true,
    startBtnStyle: TextStyle = MaterialTheme.typography.h3.copy(color = MaterialTheme.colors.onBackground),
    endBtnStyle: TextStyle = MaterialTheme.typography.h3.copy(color = MaterialTheme.colors.onBackground),
    onStartBtnClick: (() -> Unit)? = null,
    endBtnEnabled: Boolean = true,
    onEndBtnClick: (() -> Unit)? = null,
    onDismiss: () -> Unit,
) {

    BasicDialog(
        content = {
            CustomEditText(
                text = text,
                hint = hint,
                isError = isError,
                errorText = errorText,
                headerText = headerText,
                textStyle = textStyle,
                modifier = Modifier.fillMaxWidth(),
                onTextChanged = onTextChange
            )
        },
        openDialog = openDialog,
        startBtnEnabled = startBtnEnabled,
        startBtnStyle = startBtnStyle,
        onStartBtnClick = onStartBtnClick,
        startBtnText = startBtnText,
        endBtnText = endBtnText,
        endBtnEnabled = endBtnEnabled,
        endBtnStyle = endBtnStyle,
        dismissAfterClickStartBtn = dismissAfterClickStartBtn,
        onEndBtnClick = onEndBtnClick,
        onDismiss = onDismiss
    )
}

@Composable
fun TextDialogWithTwoButtons(
    titleText: String,
    openDialog: Boolean,
    startBtnText: String,
    textStyle: TextStyle = MaterialTheme.typography.h2.copy(color = MaterialTheme.colors.onBackground),
    startBtnEnabled: Boolean = true,
    startBtnStyle: TextStyle = MaterialTheme.typography.h3.copy(color = MaterialTheme.colors.onBackground),
    onStartBtnClick: (() -> Unit)? = null,
    endBtnText: String,
    endBtnEnabled: Boolean = true,
    endBtnStyle: TextStyle = MaterialTheme.typography.h3.copy(color = MaterialTheme.colors.onBackground),
    onEndBtnClick: (() -> Unit)? = null,
    onDismiss: () -> Unit,
) {

    BasicDialog(
        content = {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = titleText,
                style = textStyle
            )
        },
        openDialog = openDialog,
        startBtnEnabled = startBtnEnabled,
        startBtnStyle = startBtnStyle,
        onStartBtnClick = onStartBtnClick,
        startBtnText = startBtnText,
        endBtnText = endBtnText,
        endBtnEnabled = endBtnEnabled,
        endBtnStyle = endBtnStyle,
        onEndBtnClick = onEndBtnClick,
        onDismiss = onDismiss
    )
}

@Composable
fun BasicDialog(
    content: @Composable () -> Unit,
    openDialog: Boolean,
    startBtnText: String,
    endBtnText: String,
    startBtnEnabled: Boolean = true,
    startBtnStyle: TextStyle = MaterialTheme.typography.h3.copy(color = MaterialTheme.colors.onBackground),
    onStartBtnClick: (() -> Unit)? = null,
    dismissAfterClickStartBtn: Boolean = true,
    endBtnEnabled: Boolean = true,
    endBtnStyle: TextStyle = MaterialTheme.typography.h3.copy(color = MaterialTheme.colors.onBackground),
    onEndBtnClick: (() -> Unit)? = null,
    onDismiss: () -> Unit,
) {
    if (openDialog) {
        AlertDialog(
            shape = RoundedCornerShape(16.dp),
            onDismissRequest = onDismiss,
            text = content,
            buttons = {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    TextButton(
                        modifier = Modifier
                            .weight(1f)
                            .padding(8.dp),
                        enabled = startBtnEnabled,
                        onClick = {
                            onStartBtnClick?.invoke()
                            if (dismissAfterClickStartBtn) onDismiss()
                        }
                    ) {
                        Text(startBtnText, style = startBtnStyle)
                    }

                    TextButton(
                        modifier = Modifier
                            .weight(1f)
                            .padding(8.dp),
                        enabled = endBtnEnabled,
                        onClick = {
                            onEndBtnClick?.invoke()
                            onDismiss()
                        }
                    ) {
                        Text(endBtnText, style = endBtnStyle)
                    }

                }
            }
        )
    }
}