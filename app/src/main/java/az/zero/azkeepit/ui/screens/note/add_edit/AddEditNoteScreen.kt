package az.zero.azkeepit.ui.screens.note.add_edit

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Done
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import az.zero.azkeepit.R
import az.zero.azkeepit.ui.composables.*
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
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
    val note = state.note
    val scope = rememberCoroutineScope()
    val focusManager = LocalFocusManager.current
    val bottomState = rememberModalBottomSheetState(ModalBottomSheetValue.Hidden)

    BackHandler(
        enabled = bottomState.isVisible
    ) {
        scope.launch {
            bottomState.hide()
        }
    }

    LaunchedEffect(state.shouldPopUp) {
        if (state.shouldPopUp) {
            focusManager.clearFocus()
            navigator.popBackStack()
        }
    }

    ModalBottomSheetLayout(
        sheetElevation = 0.dp,
        sheetState = bottomState,
        sheetShape = RoundTopOnly(),
        scrimColor = Color.Transparent ,
        sheetBackgroundColor = MaterialTheme.colors.background,
        sheetContent = {
            SelectFolderBottomSheet(
                uiFolders = state.allFolders,
                titleText = stringResource(id = R.string.select_folder),
                onDismiss = { scope.launch { bottomState.hide() } },
                onClick = { viewModel.addNoteToFolder(it) }
            )
        }
    ) {
        Scaffold(
            topBar = {
                AddEditHeader(
                    saveEnabled = state.isSaveActive,
                    deleteEnabled = !state.isNoteNew,
                    onDoneClick = viewModel::saveNote,
                    onBackPressed = viewModel::onBackPressed,
                    onDeleteClick = {
                        focusManager.clearFocus()
                        viewModel.changeDialogOpenState(isOpened = true)
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

                DeleteNoteDialog(
                    openDialog = state.isDeleteDialogOpened,
                    onDismiss = { viewModel.changeDialogOpenState(isOpened = false) },
                    onDeleteClick = viewModel::deleteNote
                )

                TransparentHintTextField(
                    textModifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    text = note.title,
                    hint = stringResource(R.string.title),
                    singleLine = true,
                    maxLines = 1,
                    textStyle = MaterialTheme.typography.h1.copy(
                        color = MaterialTheme.colors.onBackground
                    ),
                    onValueChanged = viewModel::updateTitle
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        modifier = Modifier.padding(vertical = 8.dp),
                        text = "${note.longDateTime} | ${state.numberOfWordsForContent}",
                        style = MaterialTheme.typography.body2
                    )

                    TextButton(
                        onClick = {
                            focusManager.clearFocus()
                            scope.launch { bottomState.show() }
                        }
                    ) {
                        Text(
                            modifier = Modifier.padding(vertical = 8.dp),
                            text = note.ownerUiFolder?.name ?: stringResource(R.string.select_folder),
                            style = MaterialTheme.typography.body2,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }

                TransparentHintTextField(
                    textModifier = Modifier
                        .fillMaxSize(),
                    text = note.content,
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

}

@Composable
fun AddEditHeader(
    onBackPressed: () -> Unit,
    saveEnabled: Boolean = true,
    deleteEnabled: Boolean = false,
    onDoneClick: () -> Unit,
    onDeleteClick: () -> Unit,
) {
    HeaderWithBackBtn(
        text = "",
        elevation = 0.dp,
        onBackPressed = onBackPressed,
        actions = {

            if (deleteEnabled) {
                IconButton(
                    modifier = Modifier.mirror(),
                    onClick = onDeleteClick
                ) {
                    Icon(
                        Icons.Filled.Delete,
                        stringResource(id = R.string.delete),
                        tint = MaterialTheme.colors.onBackground
                    )
                }
            }

            IconButton(
                modifier = Modifier.mirror(),
                enabled = saveEnabled,
                onClick = onDoneClick
            ) {
                Icon(
                    Icons.Filled.Done,
                    stringResource(id = R.string.done),
                    tint = if (saveEnabled) MaterialTheme.colors.onBackground else Color.Gray,
                )
            }


        }
    )
}

@Composable
fun DeleteNoteDialog(
    openDialog: Boolean,
    onDismiss: () -> Unit,
    onDeleteClick: () -> Unit,
) {
    TextDialogWithTwoButtons(
        titleText = stringResource(id = R.string.are_you_sure_you_want_to_delete_this_note),
        openDialog = openDialog,
        startBtnText = stringResource(id = R.string.delete),
        onStartBtnClick = onDeleteClick,
        startBtnStyle = MaterialTheme.typography.h3.copy(color = Color.Red),
        endBtnText = stringResource(id = R.string.cancel),
        onDismiss = onDismiss
    )
}