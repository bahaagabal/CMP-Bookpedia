package com.plcoding.bookpedia.app

import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import com.plcoding.bookpedia.book.presentation.book_details.BookDetailScreenRoot
import com.plcoding.bookpedia.book.presentation.book_details.BookDetailsAction
import com.plcoding.bookpedia.book.presentation.book_details.BookDetailsViewModel
import org.jetbrains.compose.ui.tooling.preview.Preview
import com.plcoding.bookpedia.book.presentation.book_list.BookListScreenRoot
import com.plcoding.bookpedia.book.presentation.book_list.BookListViewModel
import com.plcoding.bookpedia.book.presentation.book_list.SelectedBookViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
@Preview
fun App() {

    MaterialTheme {
        val navController = rememberNavController()
        NavHost(
            navController = navController,
            startDestination = Route.BookGraph
        ) {

            navigation<Route.BookGraph>(
                startDestination = Route.BookList
            ) {

                composable<Route.BookList>(
                    exitTransition = { slideOutHorizontally() },
                    popEnterTransition = { slideInHorizontally() }
                ) {
                    val viewModel = koinViewModel<BookListViewModel>()
                    val selectedBookViewModel =
                        it.sharedKoinViewModel<SelectedBookViewModel>(navController)

                    LaunchedEffect(true) {
                        selectedBookViewModel.onBookSelected(null)
                    }

                    BookListScreenRoot(
                        bookListViewModel = viewModel,
                        onBookItemClicked = {
                            selectedBookViewModel.onBookSelected(it)
                            navController.navigate(Route.BookDetails(it.id))
                        })

                }

                composable<Route.BookDetails>(
                    exitTransition = {
                        slideOutHorizontally { initialOffset ->
                            initialOffset
                        }
                    },
                    popEnterTransition = {
                        slideInHorizontally { initialOffset ->
                            initialOffset

                        }
                    }
                ) { entry ->

                    val selectedBookViewModel =
                        entry.sharedKoinViewModel<SelectedBookViewModel>(navController)

                    val selectedBook by
                    selectedBookViewModel.selectedBook.collectAsStateWithLifecycle()

                    val viewModel = koinViewModel<BookDetailsViewModel>()

                    LaunchedEffect(selectedBook) {
                        selectedBook?.let {
                            viewModel.onAction(BookDetailsAction.OnSelectedBookChange(it))
                        }
                    }


                    BookDetailScreenRoot(
                        viewModel = viewModel
                    ) {
                        navController.navigateUp()
                    }

                }
            }
        }

    }
}

@Composable
private inline fun <reified T : ViewModel> NavBackStackEntry.sharedKoinViewModel(
    navController: NavController
): T {

    val navGraphRoute = destination.parent?.route ?: return koinViewModel<T>()
    val parentEntry = remember(this) {
        navController.getBackStackEntry(navGraphRoute)
    }
    return koinViewModel(
        viewModelStoreOwner = parentEntry
    )
}