package az.zero.azkeepit.ui.screens.note.add_edit

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import az.zero.azkeepit.R
import az.zero.azkeepit.data.local.entities.Folder
import az.zero.azkeepit.ui.composables.BasicHeaderWithBackBtn
import az.zero.azkeepit.ui.composables.TransparentHintTextField
import az.zero.azkeepit.ui.composables.clickableSafeClick
import az.zero.azkeepit.ui.theme.bgColor
import az.zero.azkeepit.ui.theme.cardBgColor
import az.zero.azkeepit.ui.theme.selectedColor
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
    val bottomSheetScaffoldState = rememberBottomSheetScaffoldState()
    val scope = rememberCoroutineScope()
    val focusManager = LocalFocusManager.current

    BackHandler(
        enabled = bottomSheetScaffoldState.bottomSheetState.isExpanded
    ) {
        scope.launch {
            bottomSheetScaffoldState.bottomSheetState.collapse()
        }
    }

    BottomSheetScaffold(
        scaffoldState = bottomSheetScaffoldState,
        sheetContent = {
            AddEditBottomSheet(
                folders = state.allFolders
            ) {
                scope.launch {
                    bottomSheetScaffoldState.bottomSheetState.collapse()
                }
                viewModel.addNoteToFolder(it)
            }
        },
        sheetPeekHeight = 0.dp,
        topBar = {
            AddEditHeader(
                enabled = state.isSaveActive,
                onDoneClick = {
                    focusManager.clearFocus()
                    viewModel.saveNote()
                    navigator.popBackStack()
                },
                onBackPressed = {
                    focusManager.clearFocus()
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

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    modifier = Modifier.padding(vertical = 8.dp),
                    text = "${state.dateTime} | ${state.numberOfWordsForContent}",
                    style = MaterialTheme.typography.body2
                )

                TextButton(onClick = {
                    scope.launch {
                        bottomSheetScaffoldState.bottomSheetState.expand()
                    }
                }) {
                    Text(
                        modifier = Modifier.padding(vertical = 8.dp),
                        text = state.folder?.name ?: stringResource(R.string.select_folder),
                        style = MaterialTheme.typography.body2
                    )
                }
            }

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
fun AddEditBottomSheet(
    folders: List<Folder>,
    onClick: (folder: Folder) -> Unit,
) {
    Column(
        modifier = Modifier
            .background(bgColor)
            .clip(RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp))
            .background(cardBgColor)
    ) {
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            text = stringResource(R.string.select_folder),
            style = MaterialTheme.typography.h3.copy(
                color = selectedColor
            ),
            textAlign = TextAlign.Center,
        )

        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            items(folders) {
                BottomSheetFolderItem(
                    folder = it,
                    onClick = onClick
                )
            }
            item { Spacer(modifier = Modifier.height(20.dp)) }
        }
    }

}

@Composable
fun BottomSheetFolderItem(
    modifier: Modifier = Modifier,
    folder: Folder,
    onClick: (folder: Folder) -> Unit,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickableSafeClick(onClick = { onClick(folder) })
            .padding(horizontal = 8.dp, vertical = 12.dp)
    ) {
        Text(
            modifier = Modifier,
            text = folder.name,
            style = MaterialTheme.typography.body2
        )
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
