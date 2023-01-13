package az.zero.azkeepit.ui.composables

import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import az.zero.azkeepit.R
import az.zero.azkeepit.util.singleLineValue

@Composable
fun HeaderWithBackBtn(
    modifier: Modifier = Modifier,
    text: String,
    elevation: Dp = 0.dp,
    onBackPressed: (() -> Unit)? = null,
    backgroundColor: Color = MaterialTheme.colors.background,
    actions: @Composable RowScope.() -> Unit = {},
) {

    BasicHeaderWithBackBtn(
        modifier = modifier,
        textContent = {
            if (text.isEmpty()) Unit
            else {
                Text(
                    text = text,
                    color = MaterialTheme.colors.onPrimary,
                    maxLines = singleLineValue,
                    overflow = TextOverflow.Ellipsis
                )
            }
        },
        elevation = elevation,
        onBackPressed = onBackPressed,
        backgroundColor = backgroundColor,
        actions = actions
    )
}

@Composable
fun BasicHeaderWithBackBtn(
    modifier: Modifier = Modifier,
    textContent: @Composable () -> Unit,
    elevation: Dp = 0.dp,
    onBackPressed: (() -> Unit)? = null,
    backgroundColor: Color = MaterialTheme.colors.background,
    actions: @Composable RowScope.() -> Unit = {},
) {
    TopAppBar(
        modifier = modifier.fillMaxWidth(),
        elevation = elevation,
        title = textContent,
        actions = actions,
        backgroundColor = backgroundColor,
        navigationIcon = if (onBackPressed == null) null
        else {
            {
                IconButton(
                    onClick = { onBackPressed.invoke() }) {
                    Icon(
                        Icons.Filled.ArrowBack,
                        stringResource(id = R.string.back),
                        tint = MaterialTheme.colors.onBackground,
                        modifier = Modifier.mirror()
                    )
                }
            }
        }
    )
}