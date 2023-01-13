package az.zero.azkeepit.ui.screens.search

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import az.zero.azkeepit.R
import az.zero.azkeepit.ui.composables.BasicHeaderWithBackBtn
import az.zero.azkeepit.ui.composables.TextWithClearIcon
import az.zero.azkeepit.ui.theme.AZKeepItTheme
import az.zero.azkeepit.ui.theme.bgColor
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@Composable
@Destination
fun SearchScreen(
    viewModel: SearchViewModel = hiltViewModel(),
    navigator: DestinationsNavigator,
) {
    var searchQuery by rememberSaveable { mutableStateOf("") }
    val focusManager = LocalFocusManager.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(bgColor)
    ) {
        SearchHeader(
            text = searchQuery,
            onTextValueChanged = { searchQuery = it },
            onClearClick = { searchQuery = "" },
            onBackPressed = {
                focusManager.clearFocus()
                navigator.popBackStack()
            }
        )

    }
}

@Composable
fun SearchHeader(
    text: String,
    onTextValueChanged: (String) -> Unit,
    onBackPressed: () -> Unit,
    onClearClick: () -> Unit,
) {

    BasicHeaderWithBackBtn(
        onBackPressed = onBackPressed,
        textContent = {
            TextWithClearIcon(
                modifier = Modifier.background(MaterialTheme.colors.background),
                text = text,
                hint = stringResource(id = R.string.search),
                onClearClick = onClearClick,
                onTextValueChanged = onTextValueChanged
            )
        }
    )
}

@Preview(showBackground = true)
@Composable
fun Preview() {
    AZKeepItTheme {
        var text by remember {
            mutableStateOf("")
        }

        BasicHeaderWithBackBtn(
            onBackPressed = { },
            textContent = {
                TextWithClearIcon(
                    modifier = Modifier.background(MaterialTheme.colors.background),
                    text = text,
                    onClearClick = {
                        text = ""
                    },
                    onTextValueChanged = {
                        text = it
                    }
                )
            }
        )
    }
}
