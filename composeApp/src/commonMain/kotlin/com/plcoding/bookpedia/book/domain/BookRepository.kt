package com.plcoding.bookpedia.book.domain

import com.plcoding.bookpedia.core.domain.DataError
import com.plcoding.bookpedia.core.domain.Result

interface BookRepository {

    suspend fun searchBooks(query: String): Result<List<Book>, DataError>
}