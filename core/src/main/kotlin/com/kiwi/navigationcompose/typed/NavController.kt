package com.kiwi.navigationcompose.typed

import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.Navigator
import androidx.navigation.PopUpToBuilder
import kotlinx.serialization.ExperimentalSerializationApi

/**
 * Navigates to the passed Route.
 */
public fun NavController.navigate(
	route: Route,
	navOptions: NavOptions? = null,
	navigatorExtras: Navigator.Extras? = null,
) {
	navigate(route.url, navOptions, navigatorExtras)
}

@ExperimentalSerializationApi
public inline fun <reified T : Destination> NavOptions.Builder.setPopUpTo(
	inclusive: Boolean,
	saveState: Boolean = false,
): NavOptions.Builder {
	return setPopUpTo(createRoutePattern<T>(), inclusive, saveState)
}

/**
 * Navigates to the passed Route.
 */
public fun NavController.navigate(
	route: Route,
	builder: NavOptionsBuilder.() -> Unit,
) {
	navigate(route.url, builder)
}

@ExperimentalSerializationApi
public inline fun <reified T : Destination> NavOptionsBuilder.popUpTo(
	noinline popUpToBuilder: PopUpToBuilder.() -> Unit = {},
) {
	popUpTo(createRoutePattern<T>(), popUpToBuilder)
}
