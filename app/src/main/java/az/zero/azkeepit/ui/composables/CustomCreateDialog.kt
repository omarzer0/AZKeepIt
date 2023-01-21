package az.zero.azkeepit.ui.composables

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import az.zero.azkeepit.R

@Composable
fun CustomCreateDialog(
    openDialog: Boolean,
    headerText: String,
    onDismiss: () -> Unit,
    onCreateClick: (name: String) -> Unit,
) {
    var x by rememberSaveable {
        mutableStateOf("")
    }
    var text by rememberSaveable { mutableStateOf("") }
    val isStartBtnEnabled by remember { derivedStateOf { text.isNotBlank() } }
    val startBtnColor = if (isStartBtnEnabled) MaterialTheme.colors.onBackground else Color.Gray

    EtDialogWithTwoButtons(
        text = text,
        headerText = headerText,
        onTextChange = { text = it },
        openDialog = openDialog,
        startBtnText = stringResource(id = R.string.create),
        onStartBtnClick = { onCreateClick(text) },
        startBtnEnabled = isStartBtnEnabled,
        startBtnStyle = MaterialTheme.typography.h3.copy(color = startBtnColor),
        endBtnText = stringResource(id = R.string.cancel),
        onDismiss = {
            text = ""
            onDismiss()
        }
    )
}