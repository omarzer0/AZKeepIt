package az.zero.azkeepit.ui.screens.note.add_edit

import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import az.zero.azkeepit.R
import az.zero.azkeepit.domain.mappers.UiFolder
import az.zero.azkeepit.domain.mappers.UiNote
import az.zero.azkeepit.ui.composables.*
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
    val note = state.note
    val scope = rememberCoroutineScope()
    val focusManager = LocalFocusManager.current
    val bottomState = rememberBottomSheetScaffoldState()

    BackHandler(
        enabled = bottomState.bottomSheetState.isExpanded
    ) {
        scope.launch {
            bottomState.bottomSheetState.collapse()
        }
    }

    LaunchedEffect(state.shouldPopUp) {
        if (state.shouldPopUp) {
            focusManager.clearFocus()
            navigator.popBackStack()
        }
    }

    DeleteNoteDialog(
        openDialog = state.isDeleteDialogOpened,
        onDismiss = { viewModel.changeDeleteDialogOpenState(isOpened = false) },
        onDeleteClick = viewModel::deleteNote
    )

    SelectFolderDialog(
        openDialog = state.isSelectFolderDialogOpened,
        folders = state.allFolders,
        onDismiss = { viewModel.changeSelectFolderDialogOpenState(isOpened = false) },
        onFolderClick = viewModel::addNoteToFolder
    )

    ChangeSystemBarsColorAndRevertWhenClose(
        key = note.color,
        statusBarColors = EnterExitColors(
            enterColor = note.color,
            exitStatusColor = MaterialTheme.colors.background
        )
    )

    val galleryLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.PickMultipleVisualMedia()
    ) { uriList ->
        viewModel.addImages(uriList)
    }

    BottomSheetScaffold(
        scaffoldState = bottomState,
        sheetPeekHeight = 120.dp,
        backgroundColor = note.color,
        sheetShape = RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp),
        sheetContent = {
            AddEditBottomSheet(
                isNoteLocked = state.note.isLocked,
                isNewNote = state.isNoteNew,
                currentlySelectedColor = note.color,
                onLockOrUnlockClick = viewModel::updateIsLocked,
                onAddImagesClick = {
                    galleryLauncher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                },
                onDismiss = { scope.launch { bottomState.bottomSheetState.collapse() } },
                onAddFolderClick = { viewModel.changeSelectFolderDialogOpenState(isOpened = true) },
                onColorSelect = viewModel::updateNoteColor,
                onDragClick = {
                    scope.launch {
                        if (bottomState.bottomSheetState.isCollapsed) bottomState.bottomSheetState.expand()
                        else bottomState.bottomSheetState.collapse()
                    }
                }
            )
        },
        topBar = {
            AddEditHeader(
                backgroundColor = note.color,
                onBackPressed = viewModel::onBackPressed,
                saveEnabled = state.isSaveActive,
                onDoneClick = viewModel::saveNote,
            )
        },
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .background(note.color)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
        ) {

            TransparentHintTextField(
                textModifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                text = note.title,
                hint = stringResource(R.string.title),
                singleLine = true,
                maxLines = 1,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                textStyle = MaterialTheme.typography.h1.copy(color = note.color.getCorrectLightOrDarkColor()),
                onValueChanged = viewModel::updateTitle
            )

            TimeWithSelectFolder(
                modifier = Modifier.fillMaxWidth(),
                note = note,
                numberOfWordsForContent = state.numberOfWordsForContent,
//                onFolderClick = {
//                    focusManager.clearFocus()
//                    scope.launch { bottomState.bottomSheetState.expand() }
//                }
            )

            if (note.images.isNotEmpty()) {
                SlidingImage(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    dataUris = note.images,
                    onDeleteImageClick = viewModel::removeImage
                )
                Spacer(modifier = Modifier.height(16.dp))
            }

            TransparentHintTextField(
                textModifier = Modifier
                    .fillMaxSize(),
                text = note.content,
                hint = stringResource(R.string.content),
                onValueChanged = viewModel::updateContent,
                textStyle = MaterialTheme.typography.body1.copy(color = note.color.getCorrectLightOrDarkColor())
            )

        }
    }

}


@Composable
fun AddEditBottomSheet(
    modifier: Modifier = Modifier,
    isNoteLocked: Boolean,
    isNewNote: Boolean,
    currentlySelectedColor: Color,
    backgroundColor: Color = MaterialTheme.colors.background,
    onDismiss: () -> Unit,
    onAddFolderClick: () -> Unit,
    onColorSelect: (color: Color) -> Unit,
    onLockOrUnlockClick: () -> Unit,
    onAddImagesClick: () -> Unit,
    onDragClick: () -> Unit,
) {
    Column(
        modifier = modifier.background(backgroundColor)
    ) {

        Icon(
            imageVector = Icons.Filled.DragHandle,
            stringResource(id = R.string.drag),
            tint = Color.White,
            modifier = Modifier
                .fillMaxWidth()
                .clickableSafeClick(onClick = onDragClick)
                .padding(8.dp),
        )

        Spacer(modifier = Modifier.height(8.dp))

        ColorsRow(
            modifier = Modifier.fillMaxWidth(),
            currentlySelectedColor = currentlySelectedColor,
            onClick = onColorSelect
        )

        val items = addEditBottomSheetItems(
            isNoteLocked = isNoteLocked,
            isNewNote = isNewNote,
            onAddFolderClick = onAddFolderClick,
            onLockOrUnlockClick = onLockOrUnlockClick,
            onAddImagesClick = onAddImagesClick
        )

        BottomSheetWithItems(
            items = items,
            onDismiss = onDismiss,
            backgroundColor = backgroundColor
        )
    }
}


@Composable
fun addEditBottomSheetItems(
    isNoteLocked: Boolean,
    isNewNote: Boolean,
    onAddFolderClick: () -> Unit,
    onLockOrUnlockClick: () -> Unit,
    onAddImagesClick: () -> Unit,
): List<BottomSheetDateItem> {

    val list = mutableListOf<BottomSheetDateItem>()
    val addNoteToFolder = BottomSheetDateItem(
        title = stringResource(id = R.string.add_note_to_folder),
        imageVector = Icons.Filled.Add,
        iconContentDescription = stringResource(id = R.string.add_note_to_folder),
        onClick = onAddFolderClick
    )

    val deleteNote = BottomSheetDateItem(
        title = stringResource(id = R.string.delete_note),
        imageVector = Icons.Filled.Delete,
        iconContentDescription = stringResource(id = R.string.delete_note),
        onClick = onAddFolderClick
    )

    val lockOrUnlockNote = BottomSheetDateItem(
        title = if (isNoteLocked) stringResource(id = R.string.lock) else stringResource(id = R.string.unlock),
        imageVector = if (isNoteLocked) Icons.Filled.Lock else Icons.Filled.LockOpen,
        iconContentDescription = if (isNoteLocked) stringResource(id = R.string.lock)
        else stringResource(id = R.string.unlock),
        onClick = onLockOrUnlockClick,
        dismissAfterClick = false
    )

    val addImages = BottomSheetDateItem(
        title = stringResource(id = R.string.add_images),
        imageVector = Icons.Filled.AddPhotoAlternate,
        iconContentDescription = stringResource(id = R.string.add_images),
        onClick = onAddImagesClick
    )

    list.addAll(listOf(addNoteToFolder, lockOrUnlockNote, addImages))
    if (!isNewNote) list.add(deleteNote)
    return list
}

@Composable
fun TimeWithSelectFolder(
    modifier: Modifier = Modifier,
    note: UiNote,
    numberOfWordsForContent: Int,
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            modifier = Modifier.padding(vertical = 8.dp),
            text = "${note.longDateTime} | $numberOfWordsForContent",
            style = MaterialTheme.typography.body2.copy(
                color = note.color.getCorrectLightOrDarkColor()
            )
        )

        Text(
            modifier = Modifier.padding(vertical = 8.dp),
            text = note.ownerUiFolder?.name ?: "",
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            style = MaterialTheme.typography.body2.copy(
                color = note.color.getCorrectLightOrDarkColor()
            )
        )
    }
}

@Composable
fun AddEditHeader(
    backgroundColor: Color,
    onBackPressed: () -> Unit,
    saveEnabled: Boolean,
    onDoneClick: () -> Unit,
) {
    HeaderWithBackBtn(
        text = "",
        elevation = 0.dp,
        onBackPressed = onBackPressed,
        backgroundColor = backgroundColor,
        actions = {
            IconButton(
                modifier = Modifier.mirror(),
                enabled = saveEnabled,
                onClick = onDoneClick
            ) {
                Icon(
                    Icons.Filled.Done,
                    stringResource(id = R.string.done),
//                    tint = if (saveEnabled) MaterialTheme.colors.onBackground else Color.Gray,
                    tint = backgroundColor.getCorrectLightOrDarkColor(isInActiveColor = saveEnabled.not()),
                )
            }
        }
    )
}


@Composable
@Stable
fun Color.getCorrectLightOrDarkColor(
    darkIcon: Boolean = this.luminance() > 0.5f,
    isInActiveColor: Boolean = false,
): Color {
    return when {
        isInActiveColor -> Color.Gray
        darkIcon -> Color.Black
        else -> Color.White
    }
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

@Composable
fun SelectFolderDialog(
    openDialog: Boolean,
    folders: List<UiFolder>,
    onDismiss: () -> Unit,
    onFolderClick: (uiFolder: UiFolder) -> Unit,
) {
    if (openDialog) {
        AlertDialog(
            shape = RoundedCornerShape(16.dp),
            onDismissRequest = onDismiss,
            text = {
                Column(
                    modifier = Modifier.height(100.dp)
                ) {
                    Text(
                        text = stringResource(id = R.string.select_folder),
                        style = MaterialTheme.typography.h2.copy(color = selectedColor)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    LazyColumn(
                        modifier = Modifier.weight(1f)
                    ) {

                        items(folders) {
                            HeaderText(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickableSafeClick(onClick = {
                                        onFolderClick(it)
                                        onDismiss()
                                    })
                                    .padding(horizontal = 8.dp, vertical = 12.dp),
                                text = it.name,
                            )
                        }
                    }

                }
            },
            buttons = {
                TextButton(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = onDismiss
                ) {
                    Text(
                        modifier = Modifier.padding(4.dp),
                        text = stringResource(id = R.string.cancel),
                        style = MaterialTheme.typography.h3.copy(color = MaterialTheme.colors.onBackground)
                    )
                }
            }

        )
    }

}