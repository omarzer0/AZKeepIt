package az.zero.azkeepit.ui.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import az.zero.azkeepit.R
import az.zero.azkeepit.domain.mappers.UiNote

@Composable
fun CustomCreateDialog(
    openDialog: Boolean,
    headerText: String,
    onDismiss: () -> Unit,
    onCreateClick: (name: String) -> Unit,
) {
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

@Composable
fun EnterNotePasswordDialog(
    openDialog: Boolean,
    uiNote: UiNote,
    onDismiss: () -> Unit,
    onCorrectPasswordClick: (uiNote: UiNote) -> Unit,
) {
    var text by rememberSaveable { mutableStateOf("") }
    val isStartBtnEnabled by remember { derivedStateOf { text.isNotBlank() } }
    val startBtnColor = if (isStartBtnEnabled) MaterialTheme.colors.onBackground else Color.Gray
    var isError by remember { mutableStateOf(false) }
    var passwordVisible by remember { mutableStateOf(false) }

    EtDialogWithTwoButtons(
        text = text,
        headerText = stringResource(id = R.string.enter_note_password),
        onTextChange = { text = it },
        openDialog = openDialog,
        isError = isError,
        textStyle = MaterialTheme.typography.h3.copy(color = MaterialTheme.colors.onBackground),
        errorText = stringResource(id = R.string.wrong_password),
        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
        startBtnText = stringResource(id = R.string.enter),
        dismissAfterClickStartBtn = false,
        onStartBtnClick = {
            if (text != uiNote.password) isError = true
            else onCorrectPasswordClick(uiNote)
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
            text = ""
            onDismiss()
        }
    )
}


@Composable
fun CustomSetPasswordDialog(
    openDialog: Boolean,
    startBtnText: String,
    textStyle: TextStyle = MaterialTheme.typography.h3.copy(color = MaterialTheme.colors.onBackground),
    hintTextStyle: TextStyle = MaterialTheme.typography.h3.copy(color = MaterialTheme.colors.onBackground),
    startBtnStyle: TextStyle = MaterialTheme.typography.h3.copy(color = MaterialTheme.colors.onBackground),
    onSetBtnClick: ((String) -> Unit)? = null,
    endBtnText: String,
    endBtnStyle: TextStyle = MaterialTheme.typography.h3.copy(color = MaterialTheme.colors.onBackground),
    onCancelClick: (() -> Unit)? = null,
    onDismiss: () -> Unit,
) {
    var password by rememberSaveable { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var confirmPassword by rememberSaveable { mutableStateOf("") }
    var confirmPasswordVisible by rememberSaveable { mutableStateOf(false) }
    val isSetButtonEnabled by remember(openDialog) {
        derivedStateOf {
            password == confirmPassword && password.isNotBlank()
        }
    }
    val setBtnColor = if (isSetButtonEnabled) MaterialTheme.colors.onBackground else Color.Gray

    val isError by remember(openDialog) {
        derivedStateOf {
            password != confirmPassword &&
                    password.trim().isNotEmpty() &&
                    confirmPassword.trim().isNotEmpty()
        }
    }
    BasicDialog(
        content = {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = stringResource(R.string.set_password_to_note),
                    style = MaterialTheme.typography.h1.copy(
                        color = MaterialTheme.colors.onBackground
                    ),
                )

                CustomEditText(
                    text = password,
                    textStyle = textStyle,
                    hintTextStyle = hintTextStyle,
                    hint = stringResource(R.string.password),
                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    onTextChanged = { password = it },
                    trailingIcon = {
                        ShowHideIcon(
                            isVisible = passwordVisible,
                            onVisibilityChange = { passwordVisible = !passwordVisible }
                        )
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Password,
                        imeAction = ImeAction.Next
                    )
                )

                Spacer(modifier = Modifier.height(8.dp))

                CustomEditText(
                    text = confirmPassword,
                    textStyle = textStyle,
                    hintTextStyle = hintTextStyle,
                    isError = isError,
                    hint = stringResource(R.string.confirm_password),
                    visualTransformation = if (confirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    onTextChanged = { confirmPassword = it },
                    trailingIcon = {
                        ShowHideIcon(
                            isVisible = confirmPasswordVisible,
                            onVisibilityChange = {
                                confirmPasswordVisible = !confirmPasswordVisible
                            }
                        )
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Password,
                        imeAction = ImeAction.Done
                    )
                )

                Spacer(modifier = Modifier.height(8.dp))
                Spacer(modifier = Modifier.height(8.dp))

            }

        },
        openDialog = openDialog,
        startBtnEnabled = isSetButtonEnabled,
        startBtnStyle = startBtnStyle.copy(color = setBtnColor),
        onStartBtnClick = { onSetBtnClick?.invoke(password) },
        startBtnText = startBtnText,
        endBtnText = endBtnText,
        endBtnEnabled = true,
        endBtnStyle = endBtnStyle,
        onEndBtnClick = onCancelClick,
        onDismiss = {
            password = ""
            confirmPassword = ""
            passwordVisible = false
            confirmPasswordVisible = false
            onDismiss()
        }

    )

}

@Composable
fun ShowHideIcon(
    isVisible: Boolean,
    visibleDescription: String = stringResource(id = R.string.show),
    nonVisibleDescription: String = stringResource(id = R.string.hide),
    onVisibilityChange: () -> Unit,
) {
    val image = if (isVisible) Icons.Filled.Visibility
    else Icons.Filled.VisibilityOff
    val description = if (isVisible) visibleDescription else nonVisibleDescription
    IconButton(
        onClick = onVisibilityChange
    ) {
        Icon(imageVector = image, description)
    }
}