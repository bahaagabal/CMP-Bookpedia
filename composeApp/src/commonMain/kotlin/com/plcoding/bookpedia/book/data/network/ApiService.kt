package com.plcoding.bookpedia.book.data.network

import com.plcoding.bookpedia.book.data.dto.SearchedBookResponseDto
import com.plcoding.bookpedia.core.domain.DataError
import com.plcoding.bookpedia.core.domain.Result

interface ApiService {

    suspend fun searchBooks(
        query: String,
        resultLimit: Int? = null
    ): Result<SearchedBookResponseDto, DataError.Remote>
}