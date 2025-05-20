package com.plcoding.bookpedia.book.data.repository

import com.plcoding.bookpedia.book.data.mapper.toBooksList
import com.plcoding.bookpedia.book.data.network.ApiService
import com.plcoding.bookpedia.book.domain.Book
import com.plcoding.bookpedia.book.domain.BookRepository
import com.plcoding.bookpedia.core.domain.DataError
import com.plcoding.bookpedia.core.domain.Result
import com.plcoding.bookpedia.core.domain.map

class BookRepositoryImpl(
    private val apiService: ApiService
) : BookRepository {

    override suspend fun searchBooks(query: String): Result<List<Book>, DataError> {
        return apiService.searchBooks(query).map {
            it.results.toBooksList()
        }
    }
}