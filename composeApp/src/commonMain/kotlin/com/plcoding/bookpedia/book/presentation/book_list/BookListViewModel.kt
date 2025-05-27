package com.plcoding.bookpedia.book.presentation.book_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.plcoding.bookpedia.book.domain.Book
import com.plcoding.bookpedia.book.domain.BookRepository
import com.plcoding.bookpedia.core.domain.onError
import com.plcoding.bookpedia.core.domain.onSuccess
import com.plcoding.bookpedia.core.presentation.toUiText
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.IO
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class BookListViewModel(private val bookRepository: BookRepository) : ViewModel() {

    private val cachedBooks = emptyList<Book>()
    private var searchJob: Job? = null
    private var favouriteBooksJob: Job? = null
    private val _state = MutableStateFlow(BookListState())
    val state = _state
        .onStart {
            if (cachedBooks.isEmpty()) {
                observeForSearchQuery()
            }
            observeForFavouriteBooks()
        }.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000L),
            _state.value

        )

    fun onAction(action: BookListAction) {
        when (action) {

            is BookListAction.OnSearchQueryChange -> {
                _state.update {
                    it.copy(searchQuery = action.query)
                }
            }

            is BookListAction.OnTabSelected -> {
                _state.update {
                    it.copy(
                        selectedTabIndex = action.index
                    )
                }
            }
        }
    }

    private fun observeForFavouriteBooks(){
        favouriteBooksJob?.cancel()
        favouriteBooksJob = bookRepository.getFavouriteBooks()
            .onEach { favouriteBooks ->
                _state.update {
                    it.copy(
                        favouriteBooks = favouriteBooks
                    )
                }
            }
            .flowOn(Dispatchers.IO)
            .launchIn(viewModelScope)
    }


    @OptIn(FlowPreview::class)
    private fun observeForSearchQuery() {

        state.map {
            it.searchQuery
        }.distinctUntilChanged()
            .debounce(500)
            .onEach { query ->

                when {
                    query.isBlank() -> {

                        _state.update {
                            it.copy(
                                searchResults = cachedBooks,
                                errorMessage = null,
                            )
                        }
                    }

                    query.length >= 2 -> {
                        searchJob?.cancel()
                        searchJob = searchBooks(query)
                    }
                }

            }.launchIn(viewModelScope)
    }

    private fun searchBooks(query: String) = viewModelScope.launch {
        _state.update {
            it.copy(isLoading = true)
        }
        bookRepository.searchBooks(query)
            .onSuccess { searchResults ->
                _state.update {
                    it.copy(
                        searchResults = searchResults,
                        errorMessage = null,
                        isLoading = false
                    )
                }

            }.onError { error ->
                _state.update {
                    it.copy(
                        searchResults = emptyList(),
                        errorMessage = error.toUiText(),
                        isLoading = false
                    )
                }
            }
    }
}