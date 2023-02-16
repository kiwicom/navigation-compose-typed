package com.kiwi.navigationcompose.typed.demo.bottomsheet

import androidx.annotation.MainThread
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.runtime.Composable
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavDeepLink
import androidx.navigation.NavGraphBuilder
import androidx.navigation.get
import com.kiwi.navigationcompose.typed.Destination
import com.kiwi.navigationcompose.typed.createNavArguments
import com.kiwi.navigationcompose.typed.createRoutePattern
import com.kiwi.navigationcompose.typed.decodeArguments
import com.kiwi.navigationcompose.typed.registerDestinationType
import kotlin.reflect.KClass
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.serializer

public fun NavGraphBuilder.bottomSheet(
	route: String,
	arguments: List<NamedNavArgument> = emptyList(),
	deepLinks: List<NavDeepLink> = emptyList(),
	content: @Composable ColumnScope.(backstackEntry: NavBackStackEntry) -> Unit,
) {
	addDestination(
		BottomSheetNavigator.Destination(
			provider[BottomSheetNavigator::class],
			content,
		).apply {
			this.route = route
			arguments.forEach { (argumentName, argument) ->
				addArgument(argumentName, argument)
			}
			deepLinks.forEach { deepLink ->
				addDeepLink(deepLink)
			}
		},
	)
}

@ExperimentalSerializationApi
@MainThread
public fun <T : Destination> NavGraphBuilder.bottomSheet(
	kClass: KClass<T>,
	serializer: KSerializer<T>,
	deepLinks: List<NavDeepLink> = emptyList(),
	content: @Composable T.(NavBackStackEntry) -> Unit,
) {
	registerDestinationType(kClass, serializer)
	bottomSheet(
		route = createRoutePattern(serializer),
		arguments = createNavArguments(serializer),
		deepLinks = deepLinks,
	) { navBackStackEntry ->
		decodeArguments(serializer, navBackStackEntry).content(navBackStackEntry)
	}
}

@ExperimentalSerializationApi
@MainThread
public inline fun <reified T : Destination> NavGraphBuilder.bottomSheet(
	deepLinks: List<NavDeepLink> = emptyList(),
	noinline content: @Composable T.(NavBackStackEntry) -> Unit,
) {
	bottomSheet(
		kClass = T::class,
		serializer = serializer(),
		deepLinks = deepLinks,
		content = content,
	)
}
