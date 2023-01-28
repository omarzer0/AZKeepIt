package az.zero.azkeepit.ui.composables

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import az.zero.azkeepit.R
import az.zero.azkeepit.ui.theme.hintTextColor
import az.zero.azkeepit.ui.theme.selectedColor

@Composable
fun CustomEditText(
    modifier: Modifier = Modifier,
    text: String,
    headerText: String = "",
    hint: String = "",
    maxLines: Int = 1,
    isError: Boolean = false,
    errorText: String? = null,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    cursorColor: Color = selectedColor,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    textStyle: TextStyle = MaterialTheme.typography.h2.copy(color = MaterialTheme.colors.onBackground),
    hintTextStyle: TextStyle = MaterialTheme.typography.h3.copy(color = hintTextColor),
    borderColor: Color = MaterialTheme.colors.onBackground,
    singleLine: Boolean = true,
    trailingIcon: @Composable (() -> Unit)? = null,
    onTextChanged: (String) -> Unit = {},
) {
    Column(modifier = modifier) {

        Spacer(modifier = Modifier.height(8.dp))

        if (headerText.isNotEmpty()) {
            Text(
                text = headerText,
                style = MaterialTheme.typography.h2
            )
            Spacer(modifier = Modifier.height(8.dp))
        }

        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = text,
            keyboardOptions = keyboardOptions,
            onValueChange = onTextChanged,
            visualTransformation = visualTransformation,
            maxLines = maxLines,
            singleLine = singleLine,
            textStyle = textStyle,
            trailingIcon = trailingIcon,
            isError = isError,
            label = { Text(text = hint, style = hintTextStyle) },
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = borderColor,
                unfocusedBorderColor = borderColor,
                focusedLabelColor = borderColor,
                unfocusedLabelColor = borderColor,
                cursorColor = cursorColor
            ),
        )

        if (isError && errorText != null) {
            Text(
                text = errorText,
                color = MaterialTheme.colors.error,
                style = MaterialTheme.typography.caption,
            )
            Spacer(modifier = Modifier.height(8.dp))
        }

    }
}