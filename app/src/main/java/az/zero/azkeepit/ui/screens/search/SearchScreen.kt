package az.zero.azkeepit.ui.screens.search

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import az.zero.azkeepit.R
import az.zero.azkeepit.ui.composables.*
import az.zero.azkeepit.ui.screens.destinations.AddEditNoteScreenDestination
import az.zero.azkeepit.ui.screens.items.NoteItem
import az.zero.azkeepit.ui.theme.bgColor
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@OptIn(ExperimentalFoundationApi::class, ExperimentalComposeUiApi::class)
@Composable
@Destination
fun SearchScreen(
    viewModel: SearchViewModel = hiltViewModel(),
    navigator: DestinationsNavigator,
) {
    val focusManager = LocalFocusManager.current
    val searchedNotes by viewModel.searchedNotes.collectAsState()
    val searchQuery by viewModel.query.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(bgColor)
    ) {
        SearchHeader(
            text = searchQuery,
            onTextValueChanged = viewModel::searchNotes,
            onClearClick = { viewModel.searchNotes("") },
            onBackPressed = {
                focusManager.clearFocus()
                navigator.popBackStack()
            }
        )

        LazyVerticalStaggeredGrid(
            modifier = Modifier
                .fillMaxSize(),
            columns = StaggeredGridCells.Fixed(2),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
        ) {

            items(searchedNotes) {
                NoteItem(note = it) {
                    navigator.navigate(
                        AddEditNoteScreenDestination(noteId = it.noteId)
                    )
                }
            }
        }

    }
}


@Composable
fun ShowMore(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickableSafeClick(onClick = onClick)
            .padding(vertical = 8.dp, horizontal = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        HeaderText(
            modifier = Modifier.weight(1f),
            text = "Show more"
        )
        Icon(
            modifier = Modifier.mirror(),
            imageVector = Icons.Filled.KeyboardArrowRight,
            contentDescription = "",
            tint = MaterialTheme.colors.onBackground
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

//
//@Composable
//private fun NestedScrollExample() {
//
//    val density = LocalDensity.current
//    val statusBarTop = WindowInsets.statusBars.getTop(density)
//
//
//    val toolbarHeight = 100.dp
//    val toolbarHeightPx = with(LocalDensity.current) { toolbarHeight.roundToPx().toFloat() }
//
//    // our offset to collapse toolbar
//    val toolbarOffsetHeightPx = remember { mutableStateOf(0f) }
//
//    val nestedScrollConnection = remember {
//        object : NestedScrollConnection {
//            override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
//                val delta = available.y
//                val newOffset = toolbarOffsetHeightPx.value + delta
//                toolbarOffsetHeightPx.value =
//                    newOffset.coerceIn(-(2 * statusBarTop + toolbarHeightPx), 0f)
//                return Offset.Zero
//            }
//        }
//    }
//    Box(
//        Modifier
//            .fillMaxSize()
//            // attach as a parent to the nested scroll system
//            .nestedScroll(nestedScrollConnection)
//    ) {
//        Column(
//            modifier = Modifier
//                .padding(
//                    PaddingValues(
//                        top = toolbarHeight + 8.dp,
//                        start = 8.dp,
//                        end = 8.dp,
//                        bottom = 8.dp
//                    )
//                )
//                .verticalScroll(rememberScrollState())
//            ,
//            verticalArrangement = Arrangement.spacedBy(8.dp)
//        ) {
//            Box(modifier = Modifier
//                .fillMaxWidth()
//                .height(2000.dp))
//        }
//        TopAppBar(modifier = Modifier
//            .height(toolbarHeight)
//            .offset { IntOffset(x = 0, y = toolbarOffsetHeightPx.value.roundToInt()) },
//            elevation = 2.dp,
//            backgroundColor = Color.White,
//            title = { Text("toolbar offset is ${toolbarOffsetHeightPx.value}") })
//    }
//}