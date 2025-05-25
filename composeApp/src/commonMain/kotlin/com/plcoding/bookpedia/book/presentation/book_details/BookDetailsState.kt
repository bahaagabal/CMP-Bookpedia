package com.plcoding.bookpedia.book.presentation.book_details

import com.plcoding.bookpedia.book.domain.Book

data class BookDetailsState(
    val isLoading: Boolean = true,
    val isFavourite: Boolean = false,
    val book: Book? = null,
)
