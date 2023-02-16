package com.kiwi.navigationcompose.typed.demo

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.kiwi.navigationcompose.typed.composable
import com.kiwi.navigationcompose.typed.createRoutePattern
import com.kiwi.navigationcompose.typed.demo.bottomsheet.BottomSheetHost
import com.kiwi.navigationcompose.typed.demo.bottomsheet.BottomSheetNavigator
import com.kiwi.navigationcompose.typed.demo.bottomsheet.bottomSheet
import com.kiwi.navigationcompose.typed.demo.screens.Demo
import com.kiwi.navigationcompose.typed.demo.screens.Home
import com.kiwi.navigationcompose.typed.demo.screens.List
import com.kiwi.navigationcompose.typed.demo.screens.NameEditBottomSheetDialog
import com.kiwi.navigationcompose.typed.demo.screens.NameEditDialog
import com.kiwi.navigationcompose.typed.demo.screens.NameEditScreen
import com.kiwi.navigationcompose.typed.demo.screens.Profile
import com.kiwi.navigationcompose.typed.dialog
import com.kiwi.navigationcompose.typed.navigate
import com.kiwi.navigationcompose.typed.navigation
import kotlinx.serialization.ExperimentalSerializationApi

@OptIn(ExperimentalSerializationApi::class)
@Composable
internal fun NavHost(navController: NavHostController, bottomSheetNavigator: BottomSheetNavigator) {
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
		navigation<Destinations.Profile>(
			startDestination = createRoutePattern<ProfileDestinations.Home>(),
		) {
			composable<ProfileDestinations.Home> { Profile(navController) }
			dialog<ProfileDestinations.NameEditDialog> { NameEditDialog(navController) }
			bottomSheet<ProfileDestinations.NameEditBottomSheetDialog> {
				NameEditBottomSheetDialog(navController)
			}
			composable<ProfileDestinations.NameEditScreen> { NameEditScreen(navController) }
		}
	}
	BottomSheetHost(bottomSheetNavigator)
}
