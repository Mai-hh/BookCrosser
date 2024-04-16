package com.huaihao.bookcrosser.ui.main.search

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Done
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.huaihao.bookcrosser.R
import com.huaihao.bookcrosser.ui.common.BookCrosserTabIndicator
import com.huaihao.bookcrosser.ui.common.LimitedOutlinedTextField
import com.huaihao.bookcrosser.util.supportWideScreen
import com.huaihao.bookcrosser.viewmodel.main.SearchEvent
import com.huaihao.bookcrosser.viewmodel.main.SearchUiState


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(uiState: SearchUiState, onEvent: (event: SearchEvent) -> Unit) {
    val sheetState = rememberBottomSheetScaffoldState()
    Surface(modifier = Modifier.supportWideScreen()) {
        BottomSheetScaffold(
            sheetPeekHeight = 200.dp,
            scaffoldState = sheetState,
            sheetContent = {
                Column(modifier = Modifier.fillMaxSize()) {

                }
            },
            topBar = {
                TopAppBar(title = {
                    Text(
                        text = "搜寻图书",
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                        modifier = Modifier.padding(end = 16.dp)
                    )
                })
            },
            modifier = Modifier.fillMaxSize()
        ) { paddingValue ->
            Column(
                modifier = Modifier
                    .padding(paddingValue)
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                SearchType(modifier = Modifier)

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp), contentAlignment = Alignment.CenterEnd
                ) {
                    LimitedOutlinedTextField(
                        label = "书名",
                        value = "",
                        onValueChange = {},
                        modifier = Modifier
                            .fillMaxWidth()
                    )

                    FilterChip(
                        selected = true,
                        onClick = { /*TODO*/ },
                        label = { Text(text = "完全匹配") },
                        modifier = Modifier.padding(end = 8.dp, top = 8.dp)
                    )
                }

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp), contentAlignment = Alignment.CenterEnd
                ) {
                    LimitedOutlinedTextField(
                        label = "作者",
                        value = "",
                        onValueChange = {},
                        modifier = Modifier
                            .fillMaxWidth()
                    )

                    FilterChip(
                        selected = true,
                        onClick = { /*TODO*/ },
                        label = { Text(text = "完全匹配") },
                        modifier = Modifier.padding(end = 8.dp, top = 8.dp)
                    )
                }

                Button(
                    onClick = { },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp)
                ) {
                    Text(text = "搜索")
                }
            }
        }
    }
}

@Composable
fun BookCard(modifier: Modifier = Modifier) {
    ConstraintLayout(modifier = modifier.height(300.dp)) {
        val (frame, image, title, action) = createRefs()
        ElevatedCard(modifier = Modifier
            .fillMaxSize()
            .constrainAs(frame) {
                top.linkTo(parent.top)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                bottom.linkTo(parent.bottom)
            }) {

        }

        Card(modifier = Modifier
            .fillMaxWidth()
            .constrainAs(image) {
                top.linkTo(frame.top)
                start.linkTo(frame.start)
                end.linkTo(frame.end)
                height = Dimension.percent(0.5f)
            }) {

        }

        Text(
            text = "书名",
            modifier = Modifier
                .constrainAs(title) {
                    top.linkTo(image.bottom)
                    start.linkTo(frame.start)
                }
                .padding(12.dp),
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )

        Button(onClick = { /*TODO*/ }, modifier = Modifier.constrainAs(action) {
            bottom.linkTo(frame.bottom)
            start.linkTo(frame.start)
        }.padding(12.dp)) {
            Text(text = "求漂")
        }
    }
}


@Composable
fun SearchType(modifier: Modifier) {
    Column(modifier = modifier, horizontalAlignment = Alignment.Start) {
        FilterChips(items = listOf("基本", "ISBN", "BCID"))
    }
}

@Composable
fun FilterChips(items: List<String>) {
    var selected by remember { mutableIntStateOf(0) }
    Row {
        items.forEachIndexed { index, item ->
            FilterChip(
                leadingIcon = {
                    AnimatedVisibility(visible = selected == index) {
                        Icon(
                            Icons.Rounded.Done,
                            contentDescription = item,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                },
                selected = (selected == index),
                onClick = {
                    selected = index
                },
                label = { Text(text = item) },
                modifier = Modifier.padding(end = 8.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun BookCardPreview() {
    MaterialTheme {
        BookCard()
    }
}

@Preview(showBackground = true)
@Composable
fun FilterChipsPreview() {
    MaterialTheme {
        FilterChips(
            items = listOf("基本", "ISBN", "BCID")
        )
    }
}


@Preview(showBackground = true)
@Composable
fun SearchTypePreview() {
    MaterialTheme {
        SearchType(Modifier)
    }
}


@Preview
@Composable
fun SearchScreenPreview() {
    MaterialTheme {
        SearchScreen(
            onEvent = {},
            uiState = SearchUiState()
        )
    }
}