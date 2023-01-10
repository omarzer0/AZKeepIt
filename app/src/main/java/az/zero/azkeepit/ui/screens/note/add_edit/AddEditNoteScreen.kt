package az.zero.azkeepit.ui.screens.note.add_edit

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import az.zero.azkeepit.R
import az.zero.azkeepit.ui.composables.BasicHeaderWithBackBtn
import az.zero.azkeepit.ui.composables.TransparentHintTextField
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@ExperimentalComposeUiApi
@ExperimentalFoundationApi
@Composable
@Destination(
    navArgsDelegate = AddEditNoteScreenArgs::class
)
fun AddEditNoteScreen(
    viewModel: AddEditNoteScreenViewModel = hiltViewModel(),
    navigator: DestinationsNavigator,
) {

    val state by viewModel.state.collectAsState()

    Scaffold(
        topBar = {
            AddEditHeader(
                enabled = state.isSaveActive,
                onDoneClick = {
                    viewModel.saveNote()
                    navigator.popBackStack()
                },
                onBackPressed = {
                    navigator.popBackStack()
                }
            )
        },
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .padding(16.dp)
                .background(MaterialTheme.colors.background),
        ) {

            TransparentHintTextField(
                textModifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                text = state.title,
                hint = stringResource(R.string.title),
                singleLine = true,
                maxLines = 1,
                textStyle = MaterialTheme.typography.h1.copy(
                    color = MaterialTheme.colors.onBackground
                ),
                onValueChanged = viewModel::updateTitle
            )

            Text(
                modifier = Modifier.padding(vertical = 8.dp),
                text = "${state.dateTime} | ${state.numberOfWordsForContent}",
                style = MaterialTheme.typography.body2
            )

            TransparentHintTextField(
                textModifier = Modifier
                    .fillMaxSize(),
                text = state.content,
                hint = stringResource(R.string.content),
                onValueChanged = viewModel::updateContent,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                textStyle = MaterialTheme.typography.body1.copy(
                    color = MaterialTheme.colors.onBackground,
                )
            )
        }

    }

}

@Composable
fun AddEditHeader(
    onBackPressed: () -> Unit,
    onDoneClick: () -> Unit,
    enabled: Boolean = true,
) {
    BasicHeaderWithBackBtn(
        text = "",
        elevation = 0.dp,
        onBackPressed = onBackPressed,
        actions = {
            IconButton(
                enabled = enabled,
                onClick = onDoneClick
            ) {
                Icon(
                    Icons.Filled.Done,
                    stringResource(id = R.string.done),
                    tint = if (enabled) MaterialTheme.colors.onBackground else Color.Gray,
                )
            }
        }
    )
}
