package com.plcoding.bookpedia.di

import com.plcoding.bookpedia.book.data.network.ApiService
import com.plcoding.bookpedia.book.data.network.ApiServiceImpl
import com.plcoding.bookpedia.book.data.repository.BookRepositoryImpl
import com.plcoding.bookpedia.book.domain.BookRepository
import com.plcoding.bookpedia.book.presentation.book_details.BookDetailsViewModel
import com.plcoding.bookpedia.book.presentation.book_list.BookListViewModel
import com.plcoding.bookpedia.book.presentation.book_list.SelectedBookViewModel
import com.plcoding.bookpedia.core.data.HttpClientFactory
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

expect val platformModule: Module

val sharedModules = module {

    single { HttpClientFactory.create(get()) }

    singleOf(::ApiServiceImpl).bind<ApiService>()

    singleOf(::BookRepositoryImpl).bind<BookRepository>()

    viewModelOf(::BookListViewModel)

    viewModelOf(::SelectedBookViewModel)

    viewModelOf(::BookDetailsViewModel)
}