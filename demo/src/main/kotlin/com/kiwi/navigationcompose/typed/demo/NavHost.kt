package com.kiwi.navigationcompose.typed.demo

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.kiwi.navigationcompose.typed.composable
import com.kiwi.navigationcompose.typed.createRoutePattern
import com.kiwi.navigationcompose.typed.demo.screens.Demo
import com.kiwi.navigationcompose.typed.demo.screens.Home
import com.kiwi.navigationcompose.typed.demo.screens.List
import com.kiwi.navigationcompose.typed.demo.screens.Profile
import com.kiwi.navigationcompose.typed.navigate
import com.kiwi.navigationcompose.typed.navigation
import kotlinx.serialization.ExperimentalSerializationApi

@OptIn(ExperimentalSerializationApi::class)
@Composable
internal fun NavHost(navController: NavHostController) {
	NavHost(
		navController = navController,
		startDestination = createRoutePattern<Destinations.Home>(),
	) {
		navigation<Destinations.Home>(
			startDestination = createRoutePattern<HomeDestinations.Home>(),
		) {
			composable<HomeDestinations.Home> { Home(navController::navigate) }
			composable<HomeDestinations.Demo> { Demo(this, navController::navigateUp) }
		}
		composable<Destinations.List> { List() }
		composable<Destinations.Profile> { Profile() }
	}
}
