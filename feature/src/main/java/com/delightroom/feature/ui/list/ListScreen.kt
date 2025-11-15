package com.delightroom.feature.ui.list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.delightroom.core_ui.ErrorPlaceholder
import com.delightroom.core_ui.LoadingIndicator
import com.delightroom.feature.viewmodel.MusicPlayerViewModel
import kotlinx.serialization.Serializable

@Serializable
object ListDestination

@Composable
fun ListScreen(
    onItemClick: (Long) -> Unit,
    modifier: Modifier = Modifier,
    viewmodel: MusicPlayerViewModel = hiltViewModel()
) {
    val lazyPagingItems = viewmodel.flowPagingSong.collectAsLazyPagingItems()

    val loadState = lazyPagingItems.loadState

    var showErrorDialog by remember { mutableStateOf(false) }
    val errorMessage = when {
        loadState.refresh is LoadState.Error -> (loadState.refresh as LoadState.Error).error.message
        loadState.append is LoadState.Error -> (loadState.append as LoadState.Error).error.message
        else -> null
    }

    LaunchedEffect(errorMessage) {
        if (errorMessage != null) showErrorDialog = true
    }

    Box(modifier = modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(lazyPagingItems.itemCount) { index ->
                lazyPagingItems[index]?.let { uiSong ->
                    ListItem(uiSong)
                }
            }

            if (loadState.append is LoadState.Loading) {
                item {
                    LoadingIndicator(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 24.dp)
                    )
                }
            }
        }

        if (loadState.refresh is LoadState.Loading) {
            LoadingIndicator(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp)
            )
        }

        if (showErrorDialog) {
            ErrorPlaceholder(errorMessage = errorMessage ?: "") { }
        }
    }
}