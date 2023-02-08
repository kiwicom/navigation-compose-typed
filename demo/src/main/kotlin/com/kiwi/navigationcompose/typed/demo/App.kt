package com.kiwi.navigationcompose.typed.demo

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.kiwi.navigationcompose.typed.Destination
import com.kiwi.navigationcompose.typed.createRoutePattern
import com.kiwi.navigationcompose.typed.navigate
import kotlinx.serialization.ExperimentalSerializationApi

@OptIn(ExperimentalMaterial3Api::class)
@Composable
public fun App() {
	MaterialTheme {
		val systemUiController = rememberSystemUiController()
		val isLight = MaterialTheme.colorScheme.background.luminance() > 0.5
		SideEffect {
			systemUiController.setSystemBarsColor(
				color = Color.Transparent,
				darkIcons = isLight,
			)
		}

		val navController = rememberNavController()
		Scaffold(
			bottomBar = {
				BottomBar(navController)
			},
		) {
			Box(Modifier.padding(it)) {
				NavHost(navController)
			}
		}
	}
}

private data class Item(
	val id: Int,
	val title: String,
	val icon: ImageVector,
	val route: String,
	val destination: Destination,
)

@OptIn(ExperimentalSerializationApi::class)
private val items = listOf(
	Item(
		0,
		"Home",
		Icons.Default.Home,
		createRoutePattern<Destinations.Home>(),
		Destinations.Home,
	),
	Item(
		1,
		"List",
		Icons.Default.List,
		createRoutePattern<Destinations.List>(),
		Destinations.List,
	),
	Item(
		2,
		"Profile",
		Icons.Default.Person,
		createRoutePattern<Destinations.Profile>(),
		Destinations.Profile,
	),
)

@OptIn(ExperimentalSerializationApi::class)
@Composable
private fun BottomBar(navController: NavHostController) {
	val navBackStackEntry by navController.currentBackStackEntryAsState()
	val currentDestination = navBackStackEntry?.destination

	NavigationBar {
		items.forEach { item ->
			NavigationBarItem(
				selected = currentDestination?.hierarchy?.any { it.route == item.route } == true,
				onClick = {
					navController.navigate(item.destination) {
						popUpTo(navController.graph.startDestinationId) {
							saveState = true
							inclusive = true
						}
						restoreState = true
					}
				},
				label = {
					Text(item.title)
				},
				icon = {
					Icon(item.icon, contentDescription = null)
				},
			)
		}
	}
}
