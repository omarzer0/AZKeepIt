package az.zero.azkeepit.ui.composables

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.lerp
import az.zero.azkeepit.R
import com.google.accompanist.pager.*
import kotlin.math.absoluteValue

@OptIn(ExperimentalPagerApi::class)
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
        val pagerState = rememberPagerState()

        HorizontalPager(
            modifier = Modifier.fillMaxSize(),
            state = pagerState,
            count = dataUris.size
        ) { page ->

            Box(
                Modifier
                    .fillMaxSize()
                    .graphicsLayer {
                        // Calculate the absolute offset for the current page from the
                        // scroll position. We use the absolute value which allows us to mirror
                        // any effects for both directions
                        val pageOffset = calculateCurrentOffsetForPage(page).absoluteValue

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
                    data = dataUris[page],
                    error = painterResource(id = R.drawable.no_note),
                    placeHolder = painterResource(id = R.drawable.lock)
                )

                if (onDeleteImageClick != null) {
                    IconButton(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(8.dp),
                        onClick = { onDeleteImageClick(dataUris[page]) }
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Close,
                            tint = MaterialTheme.colors.onBackground,
                            contentDescription = stringResource(id = R.string.remove)
                        )
                    }
                }
            }
        }

        if (dataUris.size > 1){
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
    }
}


