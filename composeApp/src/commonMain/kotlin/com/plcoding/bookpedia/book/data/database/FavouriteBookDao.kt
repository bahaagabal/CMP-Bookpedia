package com.plcoding.bookpedia.book.data.database

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface FavouriteBookDao {

    @Upsert
    suspend fun upsert(book: BookEntity)

    @Query("SELECT * FROM BookEntity")
    fun getFavouriteBooks(): Flow<List<BookEntity>>

    @Query("SELECT * FROM BookEntity WHERE id = :id ")
    suspend fun getFavouriteBook(id: String): BookEntity?

    @Query("DELETE FROM BookEntity WHERE id = :id ")
    suspend fun deleteFavouriteBook(id: String)
}