package az.zero.azkeepit.ui.screens.search

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import az.zero.azkeepit.ui.theme.bgColor
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@Composable
@Destination
fun SearchScreen(
    viewModel: SearchViewModel = hiltViewModel(),
    navigator: DestinationsNavigator,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(bgColor)
    ) {

    }
}

@Preview(showBackground = true)
@Composable
fun Preview() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.background)
    ) {
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = "Num 1",
            color = MaterialTheme.colors.onBackground
        )
    }
}