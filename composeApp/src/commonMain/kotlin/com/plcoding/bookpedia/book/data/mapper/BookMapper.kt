package com.plcoding.bookpedia.book.data.mapper

import com.plcoding.bookpedia.book.data.dto.SearchedBookDto
import com.plcoding.bookpedia.book.domain.Book


fun List<SearchedBookDto>.toBooksList() =
    this.map {
        Book(
            id = it.id,
            title = it.title,
            imageUrl = if (it.coverKey != null) {
                "https://covers.openlibrary.org/b/olid/${it.coverKey}-L.jpg"
            } else {
                "https://covers.openlibrary.org/b/id/${it.coverAlternativeKey}-L.jpg"
            },
            authors = it.authorNames.orEmpty(),
            description = null,
            languages = it.languages.orEmpty(),
            firstPublishYear = it.firstPublishYear.toString(),
            averageRating = it.ratingsAverage,
            ratingCount = it.ratingsCount,
            numPages = it.numPagesMedian,
            numEditions = it.numEditions ?: 0
        )
    }