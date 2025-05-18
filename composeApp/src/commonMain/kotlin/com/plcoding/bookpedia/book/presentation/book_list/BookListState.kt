package com.plcoding.bookpedia.book.presentation.book_list

import com.plcoding.bookpedia.book.domain.Book
import com.plcoding.bookpedia.core.presentation.UiText

data class BookListState(
    val searchQuery: String = "kotlin",
    val searchResults: List<Book> = booksList,
    val favouriteBooks: List<Book> = emptyList(),
    val isLoading: Boolean = false,
    val selectedTabIndex: Int = 0,
    val errorMessage: UiText? = null
)

val booksList = (1..100).map {
    Book(
        id = it.toString(),
        title = "Book $it",
        imageUrl = "https://picsum.photos/200",
        authors = listOf("Author $it"),
        description = "Description $it",
        languages = listOf("Language $it"),
        firstPublishYear = "2020",
        averageRating = 4.5,
        ratingCount = 100,
        numPages = 100,
        numEditions = 10
    )
}
