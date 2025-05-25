package com.plcoding.bookpedia.book.presentation.book_details

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cmp_bookpedia.composeapp.generated.resources.Res
import cmp_bookpedia.composeapp.generated.resources.description_not_available
import cmp_bookpedia.composeapp.generated.resources.languages
import cmp_bookpedia.composeapp.generated.resources.pages
import cmp_bookpedia.composeapp.generated.resources.rating
import cmp_bookpedia.composeapp.generated.resources.synopsis
import com.plcoding.bookpedia.book.presentation.book_details.components.BlurredImageBackground
import com.plcoding.bookpedia.book.presentation.book_details.components.BookChip
import com.plcoding.bookpedia.book.presentation.book_details.components.ChipSize
import com.plcoding.bookpedia.book.presentation.book_details.components.TitledContent
import com.plcoding.bookpedia.core.presentation.SandYellow
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import kotlin.math.round

@Composable
fun BookDetailScreenRoot(
    viewModel: BookDetailsViewModel = koinViewModel(),
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit
) {

    val state by viewModel.state.collectAsStateWithLifecycle()
    BookDetailsScreen(
        state,
        onAction = {
            when (it) {
                BookDetailsAction.OnBackClick -> onBackClick()
                else -> Unit
            }
            viewModel.onAction(it)
        })
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun BookDetailsScreen(
    state: BookDetailsState,
    onAction: (BookDetailsAction) -> Unit
) {

    BlurredImageBackground(
        imageUrl = state.book?.imageUrl,
        isFavourite = state.isFavourite,
        onFavouriteClick = {},
        onBackClick = { onAction(BookDetailsAction.OnBackClick) },
        modifier = Modifier.fillMaxSize()
    ) {
        state.book?.let {

            Column(
                modifier = Modifier
                    .widthIn(700.dp)
                    .fillMaxWidth()
                    .padding(
                        vertical = 16.dp,
                        horizontal = 24.dp
                    )
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Text(
                    text = it.title,
                    style = MaterialTheme.typography.headlineSmall,
                    textAlign = TextAlign.Center
                )

                Text(
                    text = it.authors.joinToString(),
                    modifier = Modifier.padding(top = 8.dp),
                    style = MaterialTheme.typography.titleMedium,
                    textAlign = TextAlign.Center
                )

                Row(
                    horizontalArrangement = Arrangement.Center
                ) {

                    it.averageRating?.let {

                        TitledContent(
                            title = stringResource(Res.string.rating),
                            modifier = Modifier.padding(
                                vertical = 8.dp
                            )
                        ) {
                            BookChip {
                                Text(
                                    text = "${round(it * 10) / 10.0}"
                                )

                                Icon(
                                    imageVector = Icons.Default.Star,
                                    contentDescription = null,
                                    tint = SandYellow
                                )
                            }

                        }

                    }

                    it.numPages?.let {

                        TitledContent(
                            title = stringResource(Res.string.pages),
                            modifier = Modifier.padding(
                                vertical = 8.dp,
                                horizontal = 16.dp
                            )
                        ) {
                            BookChip {
                                Text(
                                    text = it.toString()
                                )
                            }

                        }

                    }
                }

                if (it.languages.isNotEmpty()) {
                    TitledContent(
                        title = stringResource(Res.string.languages),
                        modifier = Modifier.padding(
                            vertical = 8.dp
                        )
                    ) {

                        FlowRow(
                            horizontalArrangement = Arrangement.Center,
                            modifier = Modifier.wrapContentSize(Alignment.Center)
                        ) {
                            it.languages.forEach {
                                BookChip(
                                    size = ChipSize.SMALL,
                                    modifier = Modifier.padding(2.dp)
                                ) {
                                    Text(
                                        text = it.uppercase(),
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                }
                            }
                        }

                    }
                }

                Text(
                    text = stringResource(Res.string.synopsis),
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.align(
                        Alignment.Start
                    )
                        .padding(
                            top = 24.dp,
                            bottom = 8.dp
                        )
                )

                when {
                    state.isLoading -> CircularProgressIndicator()
                    state.error != null -> {
                        Text(
                            text = state.error.asString(),
                            style = MaterialTheme.typography.bodyMedium,
                            textAlign = TextAlign.Justify,
                        )
                    }

                    else -> {
                        val description = state.book.description
                        Text(
                            text = if (description.isNullOrBlank()) stringResource(Res.string.description_not_available) else description,
                            style = MaterialTheme.typography.bodyMedium,
                            textAlign = TextAlign.Justify,
                            modifier = Modifier.padding(vertical = 8.dp),
                            color = if (description.isNullOrBlank()) Color.Black.copy(alpha = 0.4f) else Color.Black
                        )
                    }
                }


            }

        }
    }

}