package az.zero.azkeepit.ui.composables

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import az.zero.azkeepit.R
import az.zero.azkeepit.ui.theme.selectedColor

@Composable
fun CustomEditText(
    modifier: Modifier = Modifier,
    text: String,
    hint: String = "",
    maxLines: Int = 1,
    cursorColor: Color = selectedColor,
    textColor: Color = MaterialTheme.colors.onBackground,
    borderColor: Color = MaterialTheme.colors.onBackground,
    singleLine: Boolean = true,
    onTextChanged: (String) -> Unit = {},
) {
    Column(modifier = modifier) {

        Spacer(modifier = Modifier.height(8.dp))
        Text(text = stringResource(id = R.string.create_folder),
            style = MaterialTheme.typography.h2)
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = text,
            onValueChange = {
                onTextChanged(it)
            },
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = borderColor,
                unfocusedBorderColor = borderColor,
                focusedLabelColor = borderColor,
                unfocusedLabelColor = borderColor,
                cursorColor = cursorColor
            ),
            label = { Text(text = hint) },
            maxLines = maxLines,
            singleLine = singleLine,
            textStyle = TextStyle(color = textColor),
            modifier = Modifier.fillMaxWidth()
        )
    }
}