package com.plcoding.bookpedia.book.domain

import com.plcoding.bookpedia.core.domain.DataError
import com.plcoding.bookpedia.core.domain.EmptyResult
import com.plcoding.bookpedia.core.domain.Result
import kotlinx.coroutines.flow.Flow

interface BookRepository {

    suspend fun searchBooks(query: String): Result<List<Book>, DataError>

    suspend fun getBookDescription(bookId: String): Result<String?, DataError>

    fun getFavouriteBooks(): Flow<List<Book>>

    suspend fun deleteFavouriteBook(bookId: String)

    suspend fun addBookToFavourite(book: Book): EmptyResult<DataError.Local>

    fun isBookFavourite(bookId: String): Flow<Boolean>
}