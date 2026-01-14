package az.zero.azkeepit.ui.composables

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import az.zero.azkeepit.R

@Composable
fun EnterNotePasswordDialog(
    modifier: Modifier = Modifier,
    isError: Boolean = false,
    trimEnteredText: Boolean = true,
    onEnterBtnClick: (enteredText: String) -> Unit,
    onDismiss: () -> Unit
) {

    var text by rememberSaveable { mutableStateOf("") }
    val isStartBtnEnabled by remember { derivedStateOf { text.isNotBlank() } }
    val startBtnColor = if (isStartBtnEnabled) MaterialTheme.colors.onBackground else Color.Gray
    var passwordVisible by remember { mutableStateOf(false) }

    EtDialogWithTwoButtons(
        modifier = modifier,
        text = text,
        headerText = stringResource(id = R.string.enter_note_password),
        onTextChange = { text = it },
        openDialog = true,
        isError = isError,
        textStyle = MaterialTheme.typography.h3.copy(color = MaterialTheme.colors.onBackground),
        errorText = stringResource(id = R.string.wrong_password),
        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
        startBtnText = stringResource(id = R.string.enter),
        dismissAfterClickStartBtn = false,
        onEnterBtnClick = {
            onEnterBtnClick(if (trimEnteredText) text.trim() else text)
        },
        startBtnEnabled = isStartBtnEnabled,
        startBtnStyle = MaterialTheme.typography.h3.copy(color = startBtnColor),
        endBtnText = stringResource(id = R.string.cancel),
        trailingIcon = {
            ShowHideIcon(
                isVisible = passwordVisible,
                onVisibilityChange = {
                    passwordVisible = !passwordVisible
                }
            )
        },
        onDismiss = {
            onDismiss()
        }
    )
}