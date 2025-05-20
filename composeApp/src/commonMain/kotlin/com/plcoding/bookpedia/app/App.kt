package com.plcoding.bookpedia.app

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import org.jetbrains.compose.ui.tooling.preview.Preview
import com.plcoding.bookpedia.book.presentation.book_list.BookListScreenRoot
import com.plcoding.bookpedia.book.presentation.book_list.BookListViewModel
import com.plcoding.bookpedia.book.presentation.book_list.SelectedBookViewModel
import com.plcoding.bookpedia.core.presentation.DarkBlue
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

                composable<Route.BookList> {
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

                composable<Route.BookDetails> { entry ->
                    val args = entry.toRoute<Route.BookDetails>()

                    val selectedBookViewModel =
                        entry.sharedKoinViewModel<SelectedBookViewModel>(navController)

                    val selectedBook =
                        selectedBookViewModel.selectedBook.collectAsStateWithLifecycle()

                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "The Book Id is ${selectedBook.value?.toString()}",
                            color = DarkBlue,
                            modifier = Modifier.padding(horizontal = 16.dp)
                        )
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