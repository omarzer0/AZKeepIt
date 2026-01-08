package az.zero.azkeepit.ui.composables

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ContentAlpha
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.LocalContentAlpha
import androidx.compose.material.LocalContentColor
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.lerp
import az.zero.azkeepit.R
import kotlin.math.absoluteValue

@Composable
fun SlidingImage(
    modifier: Modifier = Modifier,
    dataUris: List<Uri>,
    activeColor: Color = LocalContentColor.current.copy(alpha = LocalContentAlpha.current),
    inactiveColor: Color = activeColor.copy(ContentAlpha.disabled),
    indicatorBackground: Brush = SolidColor(Color.Transparent),
    onDeleteImageClick: ((dataUri: Uri) -> Unit)? = null,
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.BottomCenter
    ) {
        val pagerState = rememberPagerState(pageCount = { dataUris.size })
        val shouldShowIndicator = remember(pagerState.pageCount) { pagerState.pageCount in 2..6 }
        val shouldShowNumberOfImages = remember(pagerState.pageCount) { pagerState.pageCount > 6 }
        val cornerShape = RoundedCornerShape(8.dp)
        val color = MaterialTheme.colors.background

        HorizontalPager(
            modifier = Modifier.fillMaxSize(),
            state = pagerState,
        ) { page ->

            Box(
                Modifier
                    .fillMaxSize()
                    .graphicsLayer {
                        // Calculate the absolute offset for the current page from the
                        // scroll position. We use the absolute value which allows us to mirror
                        // any effects for both directions
                        val pageOffset = (
                                (pagerState.currentPage - page) + pagerState.currentPageOffsetFraction
                                ).absoluteValue

                        // We animate the scaleX + scaleY, between 85% and 100%
                        lerp(
                            start = 0.85f,
                            stop = 1f,
                            fraction = 1f - pageOffset.coerceIn(0f, 1f)
                        ).also { scale ->
                            scaleX = scale
                            scaleY = scale
                        }

                        // We animate the alpha, between 50% and 100%
                        alpha = lerp(
                            start = 0.5f,
                            stop = 1f,
                            fraction = 1f - pageOffset.coerceIn(0f, 1f)
                        )
                    },
            ) {
                CustomImage(
                    modifier = Modifier.fillMaxSize(),
                    data = dataUris[page]
                )
            }
        }

        if (onDeleteImageClick != null) {
            IconButton(
                modifier = Modifier.align(Alignment.TopStart),
                onClick = { onDeleteImageClick(dataUris[pagerState.currentPage]) }
            ) {
                Icon(
                    imageVector = Icons.Filled.Cancel,
                    tint = color,
                    contentDescription = stringResource(id = R.string.remove)
                )
            }
        }

        if (shouldShowIndicator) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(brush = indicatorBackground),
                contentAlignment = Alignment.Center
            ) {
                HorizontalPagerIndicator(
                    modifier = Modifier.padding(8.dp),
                    pageCount = dataUris.size,
                    pagerState = pagerState,
                    activeColor = activeColor,
                    inactiveColor = inactiveColor,
                )
            }
        }

        if (shouldShowNumberOfImages) {
            Box(
                modifier = Modifier
                    .padding(8.dp)
                    .align(Alignment.TopEnd)
                    .border(
                        width = 1.dp,
                        color = color,
                        shape = cornerShape
                    )
                    .padding(4.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "${pagerState.currentPage + 1} / ${pagerState.pageCount}",
                    style = MaterialTheme.typography.caption.copy(color = color)
                )
            }
        }
    }
}

@Composable
fun HorizontalPagerIndicator(
    pagerState: PagerState,
    pageCount: Int,
    modifier: Modifier = Modifier,
    activeColor: Color = LocalContentColor.current.copy(alpha = LocalContentAlpha.current),
    inactiveColor: Color = activeColor.copy(ContentAlpha.disabled),
    indicatorWidth: androidx.compose.ui.unit.Dp = 8.dp,
    indicatorHeight: androidx.compose.ui.unit.Dp = 8.dp,
    spacing: androidx.compose.ui.unit.Dp = 8.dp,
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(spacing),
        verticalAlignment = Alignment.CenterVertically
    ) {
        repeat(pageCount) { iteration ->
            val color = if (pagerState.currentPage == iteration) activeColor else inactiveColor
            Box(
                modifier = Modifier
                    .size(width = indicatorWidth, height = indicatorHeight)
                    .clip(CircleShape)
                    .background(color)
            )
        }
    }
}