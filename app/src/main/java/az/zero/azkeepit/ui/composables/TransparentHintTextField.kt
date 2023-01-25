package az.zero.azkeepit.ui.composables

import android.view.KeyEvent.ACTION_DOWN
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import az.zero.azkeepit.ui.theme.selectedColor

@ExperimentalComposeUiApi
@Composable
fun TransparentHintTextField(
    modifier: Modifier = Modifier,
    textModifier: Modifier = Modifier,
    text: String,
    hint: String,
    textStyle: TextStyle = TextStyle(),
    hintColor: Color = Color.Gray,
    singleLine: Boolean = false,
    maxLines: Int = Int.MAX_VALUE,
    cursorBrush: Brush = SolidColor(selectedColor),
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    onValueChanged: (String) -> Unit,
) {

    val focusManager = LocalFocusManager.current
    var isFocused by remember { mutableStateOf(false) }

    Box(
        modifier = modifier
    ) {
        BasicTextField(
            modifier = textModifier
                .onKeyEvent {
                    if (it.key == Key.Tab && it.nativeKeyEvent.action == ACTION_DOWN) {
                        focusManager.moveFocus(FocusDirection.Down)
                        true
                    } else {
                        false
                    }
                }
                .onFocusChanged { isFocused = it.isFocused },
            value = text,
            onValueChange = onValueChanged,
            maxLines = maxLines,
            cursorBrush = cursorBrush,
            singleLine = singleLine,
            textStyle = textStyle,
            keyboardOptions = keyboardOptions,
            keyboardActions = KeyboardActions(
                onNext = {
                    focusManager.moveFocus(FocusDirection.Next)
                },
                onDone = { focusManager.clearFocus() }
            )
        )

        if (!isFocused && text.isBlank()) {
            Text(
                modifier = textModifier,
                text = hint,
                style = textStyle,
                color = hintColor
            )
        }
    }

}