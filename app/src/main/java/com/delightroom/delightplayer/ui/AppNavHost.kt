package com.delightroom.delightplayer.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.delightroom.feature_list.ui.ListDestination
import com.delightroom.feature_list.ui.ListScreen
import com.delightroom.feature_player.ui.PlayDestination
import com.delightroom.feature_player.ui.PlayScreen

@Composable
fun AppNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = ListDestination,
        modifier = modifier
    ) {
        composable<ListDestination> {
            ListScreen(
                onItemClick = { id ->
                    navController.navigate(PlayDestination(id))
                }
            )
        }

        composable<PlayDestination> { backStackEntry ->
            val playDestination: PlayDestination = backStackEntry.toRoute()
            PlayScreen(
                id = playDestination.id,
                onBackClick = { navController.popBackStack(ListDestination, inclusive = false) }
            )
        }
    }
}