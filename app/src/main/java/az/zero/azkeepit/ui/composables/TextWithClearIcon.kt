package az.zero.azkeepit.ui.composables

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import az.zero.azkeepit.R
import az.zero.azkeepit.ui.theme.secondaryTextColor
import az.zero.azkeepit.ui.theme.selectedColor

@Composable
fun TextWithClearIcon(
    modifier: Modifier = Modifier,
    text: String,
    hint: String = "",
    onTextValueChanged: (String) -> Unit,
    onClearClick: () -> Unit,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(48.dp)
    ) {
        val focusManager = LocalFocusManager.current
        var isHintDisplayed by remember { mutableStateOf(hint != "") }

        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            BasicTextField(
                value = text,
                maxLines = 1,
                singleLine = true,
                cursorBrush = SolidColor(selectedColor),
                onValueChange = {
                    if (text != it) {
                        onTextValueChanged(it)
                    }
                },
                textStyle = MaterialTheme.typography.h2.copy(color = MaterialTheme.colors.onBackground),
                modifier = Modifier
                    .weight(1f)
                    .onFocusChanged {
                        isHintDisplayed = !it.isFocused && text.isEmpty()
                    },
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Search,
                    keyboardType = KeyboardType.Text
                ), keyboardActions = KeyboardActions(onSearch = {
                    focusManager.clearFocus()
                })
            )

            if (!isHintDisplayed && text.isNotEmpty()) {
                IconButton(
                    onClick = {
                        onClearClick()
                    }
                ) {
                    Icon(
                        imageVector = Icons.Filled.Clear,
                        tint = MaterialTheme.colors.onBackground,
                        contentDescription = stringResource(id = R.string.clear)
                    )
                }
            }
        }

        if (isHintDisplayed) {
            Text(
                modifier = Modifier
                    .fillMaxSize()
                    .wrapContentHeight(),
                text = hint,
                style = MaterialTheme.typography.h2.copy(
                    color = secondaryTextColor
                )
            )
        }
    }
}