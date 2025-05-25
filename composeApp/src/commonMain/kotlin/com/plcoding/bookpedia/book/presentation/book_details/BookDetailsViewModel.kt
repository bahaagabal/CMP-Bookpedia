package com.plcoding.bookpedia.book.presentation.book_details

import androidx.lifecycle.ViewModel
import com.plcoding.bookpedia.book.domain.BookRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class BookDetailsViewModel(
    private val bookRepository: BookRepository
) : ViewModel() {

    private val _state = MutableStateFlow(BookDetailsState())
    val state = _state.asStateFlow()

    fun onAction(action: BookDetailsAction) {
        when (action) {
            BookDetailsAction.OnBackClick -> {}
            BookDetailsAction.OnFavouriteClick -> {}
            is BookDetailsAction.OnSelectedBookChange -> {
                _state.update {
                    it.copy(
                        book = action.book
                    )
                }
            }
        }
    }
}