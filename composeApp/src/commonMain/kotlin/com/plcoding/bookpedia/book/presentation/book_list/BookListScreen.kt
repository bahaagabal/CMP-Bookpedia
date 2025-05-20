package com.plcoding.bookpedia.book.presentation.book_list

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cmp_bookpedia.composeapp.generated.resources.Res
import cmp_bookpedia.composeapp.generated.resources.favourites
import cmp_bookpedia.composeapp.generated.resources.no_favourite_books
import cmp_bookpedia.composeapp.generated.resources.no_search_result
import cmp_bookpedia.composeapp.generated.resources.search_results
import com.plcoding.bookpedia.book.domain.Book
import com.plcoding.bookpedia.book.presentation.book_list.components.BookList
import com.plcoding.bookpedia.book.presentation.book_list.components.BookSearchBar
import com.plcoding.bookpedia.core.presentation.DarkBlue
import com.plcoding.bookpedia.core.presentation.DesertWhite
import com.plcoding.bookpedia.core.presentation.SandYellow
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel


@Composable
fun BookListScreenRoot(
    bookListViewModel: BookListViewModel = koinViewModel(),
    onBookItemClicked: (Book) -> Unit
) {

    val state by bookListViewModel.state.collectAsStateWithLifecycle()

    BookListScreen(
        state = state,
        onAction = { action ->
            when (action) {
                is BookListAction.OnBookItemClicked -> onBookItemClicked(action.book)
                else -> Unit
            }
            bookListViewModel.onAction(action)
        })
}

@Composable
fun BookListScreen(
    state: BookListState,
    onAction: (BookListAction) -> Unit
) {

    val keyboardController = LocalSoftwareKeyboardController.current
    val pagerState = rememberPagerState { 2 }
    val searchResultState = rememberLazyListState()
    val favouriteListState = rememberLazyListState()

    LaunchedEffect(state.searchResults) {
        searchResultState.animateScrollToItem(0)
    }

    LaunchedEffect(state.selectedTabIndex) {
        pagerState.animateScrollToPage(state.selectedTabIndex)
    }

    LaunchedEffect(pagerState.currentPage) {
        onAction(BookListAction.OnTabSelected(pagerState.currentPage))
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = DarkBlue)
            .statusBarsPadding(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {

        BookSearchBar(
            searchQuery = state.searchQuery,
            onSearchQueryChange = {
                onAction(
                    BookListAction.OnSearchQueryChange(
                        it
                    )
                )
            },
            onImeSearch = {
                keyboardController?.hide()
            },
            modifier = Modifier
                .widthIn(400.dp)
                .fillMaxWidth()
                .padding(16.dp)
        )

        Surface(
            shape = RoundedCornerShape(
                topStart = 32.dp,
                topEnd = 32.dp
            ),
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            color = DesertWhite
        ) {

            Column {
                TabRow(
                    selectedTabIndex = state.selectedTabIndex,
                    modifier = Modifier.padding(vertical = 12.dp)
                        .widthIn(700.dp)
                        .fillMaxWidth(),
                    containerColor = DesertWhite,
                    indicator = { tabPositions ->
                        TabRowDefaults.SecondaryIndicator(
                            modifier = Modifier.tabIndicatorOffset(tabPositions[state.selectedTabIndex]),
                            color = SandYellow
                        )
                    },

                    ) {
                    Tab(
                        selected = state.selectedTabIndex == 0,
                        onClick = {
                            onAction(BookListAction.OnTabSelected(0))
                        },

                        modifier = Modifier.weight(1f),
                        selectedContentColor = SandYellow,
                        unselectedContentColor = Color.Black.copy(alpha = 0.5f)
                    ) {
                        Text(
                            text = stringResource(Res.string.search_results),
                            modifier = Modifier.padding(vertical = 12.dp)
                        )
                    }


                    Tab(
                        selected = state.selectedTabIndex == 1,
                        onClick = {
                            onAction(BookListAction.OnTabSelected(1))
                        },
                        modifier = Modifier.weight(1f),
                        selectedContentColor = SandYellow,
                        unselectedContentColor = Color.Black.copy(alpha = 0.5f)
                    ) {
                        Text(
                            text = stringResource(Res.string.favourites),
                            modifier = Modifier.padding(vertical = 12.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))
                HorizontalPager(
                    state = pagerState,
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                ) { pagerIndex ->

                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        when (pagerIndex) {
                            0 -> {

                                if (state.isLoading) {
                                    CircularProgressIndicator()
                                } else {
                                    when {
                                        state.errorMessage != null -> {
                                            Text(
                                                text = state.errorMessage.asString(),
                                                textAlign = TextAlign.Center,
                                                style = MaterialTheme.typography.headlineSmall,
                                                color = MaterialTheme.colorScheme.error
                                            )
                                        }

                                        state.searchResults.isEmpty() -> {
                                            Text(
                                                text = stringResource(Res.string.no_search_result),
                                                textAlign = TextAlign.Center,
                                                style = MaterialTheme.typography.headlineSmall,
                                                color = MaterialTheme.colorScheme.error
                                            )

                                        }

                                        else -> {

                                            BookList(
                                                booksList = state.searchResults,
                                                onBookItemClicked = {
                                                    onAction(BookListAction.OnBookItemClicked(it))
                                                },
                                                modifier = Modifier.fillMaxSize()
                                                    .padding(vertical = 8.dp),
                                                scrollState = searchResultState

                                            )
                                        }
                                    }
                                }

                            }

                            1 -> {

                                if (state.favouriteBooks.isEmpty()) {

                                    Text(
                                        text = stringResource(Res.string.no_favourite_books),
                                        textAlign = TextAlign.Center,
                                        style = MaterialTheme.typography.headlineSmall,
                                        color = MaterialTheme.colorScheme.error
                                    )
                                } else {

                                    BookList(
                                        booksList = state.favouriteBooks,
                                        onBookItemClicked = {
                                            onAction(BookListAction.OnBookItemClicked(it))
                                        },
                                        modifier = Modifier.fillMaxSize()
                                            .padding(vertical = 8.dp),
                                        scrollState = favouriteListState

                                    )
                                }
                            }
                        }
                    }

                }
            }
        }

    }
}