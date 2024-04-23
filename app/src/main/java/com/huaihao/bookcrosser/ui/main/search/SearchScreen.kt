package com.huaihao.bookcrosser.ui.main.search

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.PinDrop
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.huaihao.bookcrosser.model.BookSearchItem
import com.huaihao.bookcrosser.model.toSearchItem
import com.huaihao.bookcrosser.ui.common.FilterChips
import com.huaihao.bookcrosser.ui.common.LimitedOutlinedTextField
import com.huaihao.bookcrosser.ui.main.Destinations.BASIC_SEARCH_ROUTE
import com.huaihao.bookcrosser.ui.main.Destinations.BCID_SEARCH_ROUTE
import com.huaihao.bookcrosser.ui.main.Destinations.ISBN_SEARCH_ROUTE
import com.huaihao.bookcrosser.util.supportWideScreen
import com.huaihao.bookcrosser.viewmodel.main.SearchEvent
import com.huaihao.bookcrosser.viewmodel.main.SearchType
import com.huaihao.bookcrosser.viewmodel.main.SearchUiState


val types = listOf(BASIC_SEARCH_ROUTE, ISBN_SEARCH_ROUTE, BCID_SEARCH_ROUTE)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(uiState: SearchUiState, onEvent: (event: SearchEvent) -> Unit) {
    val sheetState = rememberBottomSheetScaffoldState()

    LaunchedEffect(uiState.shouldExpandBottomSheet) {
        if (uiState.shouldExpandBottomSheet) {
            sheetState.bottomSheetState.expand()
            // 将 shouldExpandBottomSheet 重置为 false,避免重复展开
            onEvent(SearchEvent.ResetShouldExpandBottomSheet)
        }
    }

    Surface(modifier = Modifier.supportWideScreen()) {
        BottomSheetScaffold(
            scaffoldState = sheetState,
            sheetContent = {
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(uiState.books) { book ->
                        BookSearchCard(book = book.toSearchItem()) {
                            onEvent(SearchEvent.NavToBookMarker(book))
                        }
                    }
                }
            },
            topBar = {
                TopAppBar(title = {
                    Text(
                        text = "搜寻图书",
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
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
                var selectedScreen by remember { mutableStateOf(BASIC_SEARCH_ROUTE) }

                FilterChips(items = types, onSelected = { selected ->
                    selectedScreen = selected
                })

                when (selectedScreen) {
                    BASIC_SEARCH_ROUTE -> {
                        BasicSearchScreen(uiState, onEvent)
                    }

                    ISBN_SEARCH_ROUTE -> {
                        ISBNSearchScreen(uiState, onEvent)
                    }

                    BCID_SEARCH_ROUTE -> {
                        BCIDSearchScreen(uiState, onEvent)
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun BasicSearchScreen(
    uiState: SearchUiState = SearchUiState(),
    onEvent: (event: SearchEvent) -> Unit = {}
) {

    var title by remember { mutableStateOf("") }
    var author by remember { mutableStateOf("") }
    var matchComplete by remember {
        mutableStateOf(false)
    }

    Column {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp), contentAlignment = Alignment.CenterEnd
        ) {
            LimitedOutlinedTextField(
                label = "书名",
                value = title,
                onValueChange = {
                    title = it
                },
                modifier = Modifier
                    .fillMaxWidth()
            )
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp), contentAlignment = Alignment.CenterEnd
        ) {
            LimitedOutlinedTextField(
                label = "作者",
                value = author,
                onValueChange = { author = it },
                modifier = Modifier
                    .fillMaxWidth()
            )
        }

        FilterChip(
            selected = matchComplete,
            onClick = { matchComplete = matchComplete.not() },
            label = { Text(text = "完全匹配") },
            modifier = Modifier.padding(end = 8.dp, top = 8.dp)
        )

        Button(
            onClick = {
                onEvent(SearchEvent.Search(type = SearchType.Basic(title, author, matchComplete)))
            },
            enabled = !uiState.isSearching,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
        ) {
            if (uiState.isSearching) {
                CircularProgressIndicator(
                    modifier = Modifier.size(14.dp),
                    color = Color.White
                )
                Spacer(modifier = Modifier.width(16.dp))
            }
            Text(text = "搜索")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ISBNSearchScreen(
    uiState: SearchUiState = SearchUiState(),
    onEvent: (event: SearchEvent) -> Unit = {}
) {

    var isbn by remember {
        mutableStateOf("")
    }

    Column {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp), contentAlignment = Alignment.CenterEnd
        ) {
            LimitedOutlinedTextField(
                label = "ISBN",
                value = isbn,
                onValueChange = { isbn = it },
                modifier = Modifier
                    .fillMaxWidth()
            )
        }

        Button(
            onClick = { onEvent(SearchEvent.Search(type = SearchType.ISBN(isbn))) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
        ) {
            Text(text = "搜索")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun BCIDSearchScreen(
    uiState: SearchUiState = SearchUiState(),
    onEvent: (event: SearchEvent) -> Unit = {}
) {

    var bcid by remember {
        mutableStateOf("")
    }

    Column {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp), contentAlignment = Alignment.CenterEnd
        ) {
            LimitedOutlinedTextField(
                label = "BCID",
                value = bcid,
                onValueChange = { bcid = it },
                modifier = Modifier
                    .fillMaxWidth()
            )
        }

        Button(
            onClick = { onEvent(SearchEvent.Search(type = SearchType.BCID(bcid))) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
        ) {
            Text(text = "搜索")
        }
    }
}


@Composable
fun BookSearchCard(
    modifier: Modifier = Modifier,
    book: BookSearchItem,
    onLocateSelected: () -> Unit = {}
) {
    ConstraintLayout(
        modifier = modifier
            .heightIn(max = 300.dp)
            .padding(horizontal = 16.dp)
            .padding(vertical = 8.dp)
    ) {
        val (frame, image, title, author, description, actions) = createRefs()
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
            text = book.title,
            modifier = Modifier
                .constrainAs(title) {
                    top.linkTo(image.bottom, margin = 12.dp)
                    start.linkTo(frame.start)
                }
                .padding(horizontal = 12.dp),
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )

        Text(
            text = book.author,
            modifier = Modifier
                .constrainAs(author) {
                    top.linkTo(title.bottom)
                    start.linkTo(frame.start, margin = 12.dp)
                },
            style = MaterialTheme.typography.bodySmall,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )

        Text(
            text = book.description,
            modifier = Modifier
                .constrainAs(description) {
                    top.linkTo(author.bottom)
                    start.linkTo(frame.start)
                    end.linkTo(frame.end)
                }
                .fillMaxWidth()
                .padding(12.dp),
            style = MaterialTheme.typography.bodyMedium,
            maxLines = 3,
            overflow = TextOverflow.Ellipsis
        )

        Row(modifier = Modifier
            .constrainAs(actions) {
                top.linkTo(image.top, margin = 8.dp)
                end.linkTo(frame.end, margin = 8.dp)
            }) {
            OutlinedButton(
                onClick = { /*TODO*/ },
                colors = ButtonDefaults.outlinedButtonColors().copy(
                    containerColor = MaterialTheme.colorScheme.surfaceContainer,
                    contentColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Text(text = "求漂")
            }

            Spacer(modifier = Modifier.width(8.dp))

            IconButton(
                colors = IconButtonDefaults.iconButtonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                ),
                onClick = {
                    onLocateSelected()
                },
            ) {
                Icon(Icons.Rounded.PinDrop, contentDescription = "收藏")
            }
        }
    }
}


@Composable
fun SearchType(modifier: Modifier) {
    Column(modifier = modifier, horizontalAlignment = Alignment.Start) {
        FilterChips(items = listOf("基本", "ISBN", "BCID"))
    }
}


@Preview(showBackground = true)
@Composable
fun BookSearchCardPreview() {
    MaterialTheme {
        BookSearchCard(
            book = BookSearchItem(
                title = "书名",
                author = "作者",
                description = "描述",
                status = "状态",
                coverUrl = null
            )
        )
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