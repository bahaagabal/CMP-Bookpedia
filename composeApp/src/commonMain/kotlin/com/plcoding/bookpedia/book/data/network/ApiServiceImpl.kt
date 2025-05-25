package com.plcoding.bookpedia.book.data.network

import com.plcoding.bookpedia.book.data.dto.BookWorkDto
import com.plcoding.bookpedia.book.data.dto.SearchedBookResponseDto
import com.plcoding.bookpedia.core.data.safeCall
import com.plcoding.bookpedia.core.domain.DataError
import com.plcoding.bookpedia.core.domain.Result
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.parameter


private const val BASE_URL = "https://openlibrary.org"

class ApiServiceImpl(
    private val httpClient: HttpClient
) : ApiService {

    override suspend fun searchBooks(
        query: String,
        resultLimit: Int?
    ): Result<SearchedBookResponseDto, DataError.Remote> = safeCall {
        httpClient.get(
            urlString = "${BASE_URL}/search.json"
        ) {

            parameter("q", query)
            parameter("limit", resultLimit)
            parameter("language", "eng")
            parameter(
                "fields",
                "key,title,author_name,author_key,cover_edition_key,cover_i,ratings_average,ratings_count,first_publish_year,language,number_of_pages_median,edition_count"
            )
        }
    }

    override suspend fun getBookDescription(bookId: String): Result<BookWorkDto, DataError.Remote> = safeCall {
        httpClient.get(
            urlString = "${BASE_URL}/works/$bookId.json"
        )
    }

}