package com.kiwi.navigationcompose.typed

import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.Navigator

/**
 * Navigates to the passed Route.
 */
public fun NavController.navigate(
	destination: Route,
	navOptions: NavOptions? = null,
	navigatorExtras: Navigator.Extras? = null,
) {
	navigate(destination.url, navOptions, navigatorExtras)
}
