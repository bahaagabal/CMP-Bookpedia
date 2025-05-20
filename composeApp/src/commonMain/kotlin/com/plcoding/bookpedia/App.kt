package com.plcoding.bookpedia

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview

import cmp_bookpedia.composeapp.generated.resources.Res
import cmp_bookpedia.composeapp.generated.resources.compose_multiplatform
import com.plcoding.bookpedia.book.data.network.ApiServiceImpl
import com.plcoding.bookpedia.book.data.repository.BookRepositoryImpl
import com.plcoding.bookpedia.book.presentation.book_list.BookListScreenRoot
import com.plcoding.bookpedia.book.presentation.book_list.BookListViewModel
import com.plcoding.bookpedia.core.data.HttpClientFactory
import io.ktor.client.HttpClient
import io.ktor.client.engine.HttpClientEngine

@Composable
@Preview
fun App(engine: HttpClientEngine) {
    BookListScreenRoot(
        bookListViewModel = remember {
            BookListViewModel(
                bookRepository = BookRepositoryImpl(
                    apiService = ApiServiceImpl(
                        httpClient = HttpClientFactory.create(
                            engine
                        )
                    )
                )
            )
        },
        onBookItemClicked = {})
}