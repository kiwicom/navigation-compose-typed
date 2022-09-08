package com.kiwi.navigationcompose.typed.demo

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.kiwi.navigationcompose.typed.composable
import com.kiwi.navigationcompose.typed.createRoutePattern
import com.kiwi.navigationcompose.typed.navigate
import kotlinx.serialization.ExperimentalSerializationApi

@OptIn(ExperimentalSerializationApi::class)
@Composable
internal fun NavHost() {
	MaterialTheme {
		val navController = rememberNavController()
		NavHost(
			navController = navController,
			startDestination = createRoutePattern<Destinations.Home>(),
		) {
			composable<Destinations.Home> { Home(navController::navigate) }
			composable<Destinations.Demo> { Demo(this, navController::navigateUp) }
		}
	}
}
