package az.zero.azkeepit.ui.composables

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Tab
import androidx.compose.material.TabPosition
import androidx.compose.material.TabRow
import androidx.compose.material.TabRowDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.debugInspectorInfo
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

@Composable
fun TabPager(
    tabs: List<String>,
    modifier: Modifier = Modifier,
    initialTabPosition: Int = 0,
    tabHostModifier: Modifier = Modifier,
    tabModifier: Modifier = Modifier,
    selectedContentColor: Color = MaterialTheme.colors.primary,
    unSelectedContentColor: Color = MaterialTheme.colors.onBackground,
    tabSelectorColor: Color = Color.Black,
    tabSelectorHeight: Dp = TabRowDefaults.IndicatorHeight,
    tabHostBackgroundColor: Color = MaterialTheme.colors.background,
    animateScrollToPage: Boolean = false,
    isEditModeEnabled: Boolean = false,
    onTabChange: ((tabNumber: Int) -> Unit)? = null,
    content: @Composable (index: Int) -> Unit,
) {
    val pagerState = rememberPagerState(
        initialPage = initialTabPosition,
        pageCount = { tabs.size }
    )

    val coroutineScope = rememberCoroutineScope()

    BackHandler(enabled = pagerState.currentPage != 0 && !isEditModeEnabled) {
        coroutineScope.launch {
            pagerState.animateScrollToPage(0)
        }
    }

    LaunchedEffect(pagerState) {
        // Collect from the pager state a snapshotFlow reading the currentPage
        snapshotFlow { pagerState.currentPage }.collect { page ->
            onTabChange?.invoke(page)
        }
    }

    Column(
        modifier = modifier.fillMaxSize(),
    ) {
        AnimatedVisibility(visible = !isEditModeEnabled) {
            TabRow(
                selectedTabIndex = pagerState.currentPage,
                modifier = tabHostModifier,
                divider = {},
                backgroundColor = tabHostBackgroundColor,
                indicator = { tabPositions ->
                    TabRowDefaults.Indicator(
                        Modifier
                            .pagerTabIndicatorOffset(
                                pagerState = pagerState,
                                tabPositions = tabPositions
                            )
                            .padding(horizontal = 40.dp)
                            .clip(CircleShape),
                        color = tabSelectorColor,
                        height = tabSelectorHeight,
                    )
                }
            ) {
                tabs.forEachIndexed { index, text ->
                    Tab(
                        selected = pagerState.currentPage == index,
                        selectedContentColor = selectedContentColor,
                        unselectedContentColor = unSelectedContentColor,
                        modifier = tabModifier,
                        onClick = {
                            coroutineScope.launch {
                                if (animateScrollToPage) pagerState.animateScrollToPage(index)
                                else pagerState.scrollToPage(index)
                            }
                        },
                        text = {
                            Text(text = text, style = MaterialTheme.typography.h2)
                        },
                    )
                }
            }
        }

        HorizontalPager(
            state = pagerState,
            userScrollEnabled = !isEditModeEnabled,
            modifier = Modifier.weight(1f),
        ) { index ->
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                content = { content(index) }
            )
        }
    }
}

fun Modifier.pagerTabIndicatorOffset(
    pagerState: PagerState,
    tabPositions: List<TabPosition>,
    pageIndexMapping: (Int) -> Int = { it },
): Modifier = composed(
    inspectorInfo = debugInspectorInfo {
        name = "pagerTabIndicatorOffset"
        value = pagerState
    }
) {
    if (tabPositions.isEmpty()) return@composed this

    val currentPage = minOf(tabPositions.lastIndex, pageIndexMapping(pagerState.currentPage))
    val currentTab = tabPositions[currentPage]
    val previousTab = tabPositions.getOrNull(currentPage - 1)
    val nextTab = tabPositions.getOrNull(currentPage + 1)

    val fraction = pagerState.currentPageOffsetFraction

    val indicatorWidth = if (fraction > 0 && nextTab != null) {
        lerp(currentTab.width, nextTab.width, fraction)
    } else if (fraction < 0 && previousTab != null) {
        lerp(currentTab.width, previousTab.width, -fraction)
    } else {
        currentTab.width
    }

    val indicatorOffset = if (fraction > 0 && nextTab != null) {
        lerp(currentTab.left, nextTab.left, fraction)
    } else if (fraction < 0 && previousTab != null) {
        lerp(currentTab.left, previousTab.left, -fraction)
    } else {
        currentTab.left
    }

    fillMaxWidth()
        .wrapContentSize(Alignment.BottomStart)
        .offset(x = indicatorOffset)
        .width(indicatorWidth)
}

private fun lerp(start: Dp, stop: Dp, fraction: Float): Dp {
    return start + (stop - start) * fraction
}