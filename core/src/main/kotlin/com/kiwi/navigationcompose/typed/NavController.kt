package com.kiwi.navigationcompose.typed

import androidx.annotation.MainThread
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.Navigator
import androidx.navigation.PopUpToBuilder
import com.kiwi.navigationcompose.typed.internal.toRoute
import kotlinx.serialization.ExperimentalSerializationApi

/**
 * Navigates to the specified [Destination].
 */
@ExperimentalSerializationApi
@MainThread
public fun NavController.navigate(
	route: Destination,
	navOptions: NavOptions? = null,
	navigatorExtras: Navigator.Extras? = null,
) {
	navigate(route.toRoute(), navOptions, navigatorExtras)
}

@ExperimentalSerializationApi
public inline fun <reified T : Destination> NavOptions.Builder.setPopUpTo(
	inclusive: Boolean,
	saveState: Boolean = false,
): NavOptions.Builder = setPopUpTo(createRoutePattern<T>(), inclusive, saveState)

/**
 * Navigates to the specified [Destination].
 */
@ExperimentalSerializationApi
@MainThread
public fun NavController.navigate(
	route: Destination,
	builder: NavOptionsBuilder.() -> Unit,
) {
	navigate(route.toRoute(), builder)
}

@ExperimentalSerializationApi
public inline fun <reified T : Destination> NavOptionsBuilder.popUpTo(
	noinline popUpToBuilder: PopUpToBuilder.() -> Unit = {},
) {
	popUpTo(createRoutePattern<T>(), popUpToBuilder)
}

@ExperimentalSerializationApi
@MainThread
public inline fun <reified T : Destination> NavController.popBackStack(
	inclusive: Boolean,
	saveState: Boolean = false,
): Boolean = popBackStack(
	route = createRoutePattern<T>(),
	inclusive = inclusive,
	saveState = saveState,
)
