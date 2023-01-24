package az.zero.azkeepit.ui.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import az.zero.azkeepit.R
import az.zero.azkeepit.ui.composables.Colors.BabyBlueHex
import az.zero.azkeepit.ui.composables.Colors.LightGreenHex
import az.zero.azkeepit.ui.composables.Colors.RedOrangeHex
import az.zero.azkeepit.ui.composables.Colors.RedPinkHex
import az.zero.azkeepit.ui.composables.Colors.VioletHex

@Composable
fun ColorsRow(
    modifier: Modifier = Modifier,
    currentlySelectedColor: Color,
    onClick: (color: Color) -> Unit,
) {
    val colors = listOf(RedOrangeHex, RedPinkHex, LightGreenHex, BabyBlueHex, VioletHex).map {
        Color(it)
    }

    LazyRow(
        modifier = modifier,
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        items(colors) {
            ColorItem(
                isSelected = currentlySelectedColor == it,
                color = it,
                onClick = onClick
            )
        }
    }


}

@Composable
fun ColorItem(
    size: Dp = 50.dp,
    isSelected: Boolean,
    color: Color,
    onClick: (color: Color) -> Unit,
) {
    Box(
        modifier = Modifier
            .padding(8.dp)
    ) {
        Box(
            modifier = Modifier
                .size(size)
                .background(Color.LightGray.copy(alpha = 0.2f), shape = CircleShape)
                .border(color = Color.Gray, width = 1.dp, shape = CircleShape)
                .clip(CircleShape)
                .clickableSafeClick(onClick = { onClick((color)) })
                .padding(6.dp)
                .clip(CircleShape)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(CircleShape)
                    .background(color),
                contentAlignment = Alignment.Center
            ) {
                if (isSelected) {
                    Icon(
                        imageVector = Icons.Filled.Done,
                        contentDescription = stringResource(id = R.string.selected),
                        tint = Color.White
                    )
                }
            }

        }
    }
}

object Colors {
    const val RedOrangeHex = 0xffffab91
    const val RedPinkHex = 0xfff48fb1
    const val BabyBlueHex = 0xff81deea
    const val VioletHex = 0xffcf94da
    const val LightGreenHex = 0xffe7ed9b
}