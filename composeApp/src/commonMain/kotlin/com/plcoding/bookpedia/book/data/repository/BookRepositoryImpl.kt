package com.plcoding.bookpedia.book.data.repository

import androidx.sqlite.SQLiteException
import com.plcoding.bookpedia.book.data.database.FavouriteBookDao
import com.plcoding.bookpedia.book.data.mapper.toBook
import com.plcoding.bookpedia.book.data.mapper.toBookEntity
import com.plcoding.bookpedia.book.data.mapper.toBooksList
import com.plcoding.bookpedia.book.data.network.ApiService
import com.plcoding.bookpedia.book.domain.Book
import com.plcoding.bookpedia.book.domain.BookRepository
import com.plcoding.bookpedia.core.domain.DataError
import com.plcoding.bookpedia.core.domain.EmptyResult
import com.plcoding.bookpedia.core.domain.Result
import com.plcoding.bookpedia.core.domain.map
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class BookRepositoryImpl(
    private val apiService: ApiService,
    private val favouriteBookDao: FavouriteBookDao
) : BookRepository {

    override suspend fun searchBooks(query: String): Result<List<Book>, DataError> {
        return apiService.searchBooks(query).map {
            it.results.toBooksList()
        }
    }

    override suspend fun getBookDescription(bookId: String): Result<String?, DataError> {
        return apiService.getBookDescription(bookId)
            .map {
                it.description
            }
    }

    override fun getFavouriteBooks(): Flow<List<Book>> {
        return favouriteBookDao.getFavouriteBooks().map { booksEntities ->
            booksEntities.map {
                it.toBook()
            }
        }
    }

    override suspend fun deleteFavouriteBook(bookId: String) {
        favouriteBookDao.deleteFavouriteBook(bookId)
    }

    override suspend fun addBookToFavourite(book: Book): EmptyResult<DataError.Local> {
        return try {
            favouriteBookDao.upsert(book.toBookEntity())
            Result.Success(Unit)
        } catch (e: SQLiteException) {
            Result.Error(DataError.Local.DISK_FULL)
        }
    }

    override fun isBookFavourite(bookId: String): Flow<Boolean> {
        return favouriteBookDao
            .getFavouriteBooks()
            .map { bookEntities ->
                bookEntities.any { it.id == bookId }
            }
    }
}