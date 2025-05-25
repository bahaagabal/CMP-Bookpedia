package com.plcoding.bookpedia.book.presentation.book_details

import com.plcoding.bookpedia.book.domain.Book

sealed interface BookDetailsAction {

    data object OnBackClick : BookDetailsAction

    data object OnFavouriteClick : BookDetailsAction

    data class OnSelectedBookChange(val book: Book) : BookDetailsAction
}