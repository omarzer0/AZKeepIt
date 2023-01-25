package az.zero.azkeepit.ui.composables

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import az.zero.azkeepit.R
import coil.compose.AsyncImage
import coil.request.ImageRequest

@Composable
fun CustomImage(
    modifier: Modifier = Modifier,
    data: Any,
    placeHolder: Painter? = null,
    error: Painter? = painterResource(id = R.drawable.ic_no_image),
    contentDescription: String? = null,
    contentScale: ContentScale = ContentScale.Crop,
) {

    AsyncImage(
        modifier = modifier,
        model = ImageRequest.Builder(LocalContext.current)
            .data(data)
            .build(),
        placeholder = placeHolder,
        error = error,
        contentDescription = contentDescription,
        contentScale = contentScale,
    )
}


