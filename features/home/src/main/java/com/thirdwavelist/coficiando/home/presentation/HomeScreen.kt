package com.thirdwavelist.coficiando.home.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Card
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberImagePainter
import com.thirdwavelist.coficiando.core.domain.cafe.Cafe
import com.thirdwavelist.coficiando.core.domain.cafe.GearInfoItem
import com.thirdwavelist.coficiando.core.domain.cafe.SocialItem
import com.thirdwavelist.coficiando.core.domain.cafe.BrewInfoItem
import com.thirdwavelist.coficiando.core.domain.cafe.BeanInfoItem
import java.util.UUID

@Composable
fun HomeScreen(viewModel: HomeViewModel = viewModel()) {
    val viewState by viewModel.viewState.collectAsState()

    Surface(
        modifier = Modifier.fillMaxSize(),
    ) {
        HomeContent(
            viewState = viewState,
            modifier = Modifier.fillMaxSize(),
        )
    }
}

@Composable
fun HomeContent(viewState: HomeViewState, modifier: Modifier) {
    when (viewState) {
        HomeViewState.Error -> {

        }
        HomeViewState.Loading -> {

        }
        is HomeViewState.Success -> {
            Column(modifier = modifier) {
                CafeList(cafes = viewState.cafes, modifier = modifier)
            }
        }
    }
}

@Composable
fun CafeList(cafes: List<Cafe>, modifier: Modifier) {
    LazyColumn(
        modifier = modifier.fillMaxHeight(),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        items(items = cafes) { cafe ->
            CafeItem(cafe)
        }
    }
}

@Composable
fun CafeItem(cafe: Cafe) {
    Card(Modifier.fillMaxWidth(), elevation = 8.dp, contentColor = Color.Black) {
        Image(
            painter = rememberImagePainter(
                data = cafe.thumbnail
            ),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            alignment = Alignment.Center
        )
    }
}

@Composable
@Preview(name = "Home with cafes loaded", showBackground = true)
private fun PreviewHome() {
    HomeContent(
        modifier = Modifier.fillMaxSize(),
        viewState = HomeViewState.Success(
            listOf(
                Cafe(UUID.fromString("4e0cea4f-5b69-4a1d-9aec-d3323fbbfac6"), "OneCup", "https://assets.thirdwavelist.com/thumb/4e0cea4f-5b69-4a1d-9aec-d3323fbbfac6.jpg",
                    SocialItem("", "", ""), "",
                    GearInfoItem("", ""),
                    BeanInfoItem("", "", true, false, true, false, true),
                    BrewInfoItem(true, true, true, false, false, false))
            )
        )
    )
}