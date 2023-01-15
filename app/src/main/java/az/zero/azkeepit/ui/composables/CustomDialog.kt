package az.zero.azkeepit.ui.composables

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.AlertDialog
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp

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
    startBtnEnabled: Boolean = true,
    startBtnStyle: TextStyle = MaterialTheme.typography.h3.copy(color = MaterialTheme.colors.onBackground),
    endBtnStyle: TextStyle = MaterialTheme.typography.h3.copy(color = MaterialTheme.colors.onBackground),
    onStartBtnClick: (() -> Unit)? = null,
    endBtnEnabled: Boolean = true,
    onEndBtnClick: (() -> Unit)? = null,
    onDismiss: () -> Unit,
) {

    BasicDialog(
        textContent = {
            CustomEditText(
                text = text,
                hint = hint,
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
        textContent = {
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
    textContent: @Composable () -> Unit,
    openDialog: Boolean,
    startBtnText: String,
    endBtnText: String,
    startBtnEnabled: Boolean = true,
    startBtnStyle: TextStyle = MaterialTheme.typography.h3.copy(color = MaterialTheme.colors.onBackground),
    onStartBtnClick: (() -> Unit)? = null,
    endBtnEnabled: Boolean = true,
    endBtnStyle: TextStyle = MaterialTheme.typography.h3.copy(color = MaterialTheme.colors.onBackground),
    onEndBtnClick: (() -> Unit)? = null,
    onDismiss: () -> Unit,
) {
    if (openDialog) {
        AlertDialog(
            shape = RoundedCornerShape(16.dp),
            onDismissRequest = {
                onDismiss()
            },
            text = textContent,
            buttons = {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(all = 8.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextButton(
                        enabled = startBtnEnabled,
                        onClick = {
                            onStartBtnClick?.invoke()
                            onDismiss()
                        }
                    ) {
                        Text(startBtnText, style = startBtnStyle)
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    TextButton(
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