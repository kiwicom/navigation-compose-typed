package com.kiwi.navigationcompose.typed.demo

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.BottomAppBar
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.kiwi.navigationcompose.typed.Destination
import com.kiwi.navigationcompose.typed.createRoutePattern
import com.kiwi.navigationcompose.typed.navigate
import kotlinx.serialization.ExperimentalSerializationApi

@Composable
public fun App() {
	MaterialTheme {
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

	BottomAppBar {
		items.forEach { item ->
			BottomNavigationItem(
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
