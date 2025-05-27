package com.plcoding.bookpedia.book.presentation.book_list

import com.plcoding.bookpedia.book.domain.Book

sealed interface BookListAction {

    data class OnSearchQueryChange(val query: String) : BookListAction

    data class OnTabSelected(val index: Int) : BookListAction

}