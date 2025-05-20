package com.plcoding.bookpedia

import androidx.compose.runtime.*
import org.jetbrains.compose.ui.tooling.preview.Preview
import com.plcoding.bookpedia.book.presentation.book_list.BookListScreenRoot
import com.plcoding.bookpedia.book.presentation.book_list.BookListViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
@Preview
fun App() {

    val viewModel = koinViewModel<BookListViewModel>()
    BookListScreenRoot(
        bookListViewModel = viewModel,
        onBookItemClicked = {})
}