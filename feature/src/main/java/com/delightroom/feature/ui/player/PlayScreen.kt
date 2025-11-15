package com.delightroom.feature.ui.player

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import kotlinx.serialization.Serializable

@Serializable
data class PlayDestination(
    val id: Long
)

@Composable
fun PlayScreen(
    id: Long,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
    viewmodel: PlayViewModel = hiltViewModel()
) {

}