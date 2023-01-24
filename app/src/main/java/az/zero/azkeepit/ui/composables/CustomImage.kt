package az.zero.azkeepit.ui.composables

import android.net.Uri
import androidx.compose.foundation.Image
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
    error: Painter? = null,
    contentDescription: String? = null,
    contentScale: ContentScale = ContentScale.Crop,
) {

//    Image(
//        modifier = modifier,
//        painter = painterResource(id = R.drawable.website),
//        contentDescription = null,
//        contentScale = ContentScale.FillBounds
//    )
    AsyncImage(
        modifier = modifier,
        model = ImageRequest.Builder(LocalContext.current)
            .data(data)
//            .crossfade(true)
            .build(),
        placeholder = placeHolder,
        error = error,
        contentDescription = contentDescription,
        contentScale = contentScale
    )
}


