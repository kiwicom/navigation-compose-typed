package com.kiwi.navigationcompose.typed

import android.os.Bundle
import androidx.annotation.MainThread
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.runtime.Composable
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavDeepLink
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.compose.dialog
import androidx.navigation.compose.navigation
import androidx.navigation.navArgument
import com.kiwi.navigationcompose.typed.internal.UriBundleDecoder
import com.kiwi.navigationcompose.typed.internal.isNavTypeOptional
import kotlin.reflect.KClass
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.serializer

/**
 * Add the Composable to the NavGraphBuilder with type-safe arguments and route.
 *
 * Pass the unique destination as a generic argument T. This function is inlined, you can provide the
 * destination serialization manually using the function variant with serializer argument.
 *
 * Arguments (a Destination instance) are available as a composable lambda's receiver. You can read those
 * values directly in the lambda.
 *
 * ```
 * sealed interface Destinations : Destination {
 *      @Serializable object Home : Destinations
 *      @Serializable data class Article(val id: Int) : Destinations
 * }
 *
 * NavGraph(...) {
 *     composable<Destinations.Home> { Home() }
 *     composable<Destinations.Article> { Article(id) }
 * }
 * ```
 */
@ExperimentalSerializationApi
@MainThread
public inline fun <reified T : Destination> NavGraphBuilder.composable(
	deepLinks: List<NavDeepLink> = emptyList(),
	noinline enterTransition: (@JvmSuppressWildcards AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition?)? = null,
	noinline exitTransition: (@JvmSuppressWildcards AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition?)? = null,
	noinline popEnterTransition: (@JvmSuppressWildcards AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition?)? = enterTransition,
	noinline popExitTransition: (@JvmSuppressWildcards AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition?)? = exitTransition,
	noinline content: @Composable T.(NavBackStackEntry) -> Unit,
) {
	composable(
		kClass = T::class,
		serializer = serializer(),
		deepLinks = deepLinks,
		enterTransition = enterTransition,
		exitTransition = exitTransition,
		popEnterTransition = popEnterTransition,
		popExitTransition = popExitTransition,
		content = content,
	)
}

/**
 * Add the Composable to the NavGraphBuilder with type-safe arguments and route.
 *
 * Pass the unique destination's serializer as an argument. This is a semi-internal implementation,
 * prefer using the generic function variant.
 */
@ExperimentalSerializationApi
@MainThread
public fun <T : Destination> NavGraphBuilder.composable(
	kClass: KClass<T>,
	serializer: KSerializer<T>,
	deepLinks: List<NavDeepLink> = emptyList(),
	enterTransition: (@JvmSuppressWildcards AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition?)? = null,
	exitTransition: (@JvmSuppressWildcards AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition?)? = null,
	popEnterTransition: (@JvmSuppressWildcards AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition?)? = enterTransition,
	popExitTransition: (@JvmSuppressWildcards AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition?)? = exitTransition,
	content: @Composable T.(NavBackStackEntry) -> Unit,
) {
	registerDestinationType(kClass, serializer)
	composable(
		route = createRoutePattern(serializer),
		arguments = createNavArguments(serializer),
		enterTransition = enterTransition,
		exitTransition = exitTransition,
		popEnterTransition = popEnterTransition,
		popExitTransition = popExitTransition,
		deepLinks = deepLinks,
	) { navBackStackEntry ->
		decodeArguments(serializer, navBackStackEntry).content(navBackStackEntry)
	}
}

/**
 * Add the Dialog to the NavGraphBuilder with type-safe arguments and route.
 *
 * Pass the unique destination as generic argument T. This function is inlined, you can provide the
 * destination serialization manually using the function variant with serializer argument.
 *
 * Arguments (a Destination instance) are available as a composable lambda's receiver. You can read those
 * values directly in the lambda.
 *
 * ```
 * sealed interface Destinations : Destination {
 *      @Serializable data class DeleteArticleConfirmation(val id: Int) : Destinations
 * }
 *
 * NavGraph(...) {
 *     dialog<Destinations.DeleteArticleConfirmation> { DeleteArticleConfirmation(id) }
 * }
 * ```
 */
@ExperimentalSerializationApi
@MainThread
public inline fun <reified T : Destination> NavGraphBuilder.dialog(
	deepLinks: List<NavDeepLink> = emptyList(),
	noinline content: @Composable T.(NavBackStackEntry) -> Unit,
) {
	dialog(
		kClass = T::class,
		serializer = serializer(),
		deepLinks = deepLinks,
		content = content,
	)
}

/**
 * Add the Dialog to the NavGraphBuilder with type-safe arguments and route.
 *
 * Pass the unique destination's serializer as an argument. This is a semi-internal implementation,
 * prefer using the generic function variant.
 */
@ExperimentalSerializationApi
@MainThread
public fun <T : Destination> NavGraphBuilder.dialog(
	kClass: KClass<T>,
	serializer: KSerializer<T>,
	deepLinks: List<NavDeepLink> = emptyList(),
	content: @Composable T.(NavBackStackEntry) -> Unit,
) {
	registerDestinationType(kClass, serializer)
	dialog(
		route = createRoutePattern(serializer),
		arguments = createNavArguments(serializer),
		deepLinks = deepLinks,
	) { navBackStackEntry ->
		decodeArguments(serializer, navBackStackEntry).content(navBackStackEntry)
	}
}

/**
 * Construct a nested NavGraph with type-safe arguments and route.
 *
 * Pass the unique destination as generic argument T. This function is inlined, you can provide the
 * destination serialization manually using the function variant with serializer argument.
 *
 * The start destination is passed as a RoutePattern obtained from a relevant Destination instance.
 *
 * ```
 * sealed interface LoginDestinations : Destination {
 *     @Serializable object Home : LoginDestinations
 *     @Serializable object PasswordReset : LoginDestinations
 * }
 *
 * inline fun <reified T : Destination> loginNavigation(navController: NavController) {
 *     navigation<T>(
 *         startDestination = createRoutePattern<LoginDestinations.Home>(),
 *     ) {
 *         composable<LoginDestinations.Home> { LoginHome() }
 *         composable<LoginDestinations.PasswordReset> { PasswordReset() }
 *     }
 * }
 * ```
 */
@ExperimentalSerializationApi
public inline fun <reified T : Destination> NavGraphBuilder.navigation(
	startDestination: String,
	deepLinks: List<NavDeepLink> = emptyList(),
	noinline enterTransition: (@JvmSuppressWildcards AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition?)? = null,
	noinline exitTransition: (@JvmSuppressWildcards AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition?)? = null,
	noinline popEnterTransition: (@JvmSuppressWildcards AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition?)? = enterTransition,
	noinline popExitTransition: (@JvmSuppressWildcards AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition?)? = exitTransition,
	noinline builder: NavGraphBuilder.() -> Unit,
) {
	navigation(
		kClass = T::class,
		serializer = serializer(),
		startDestination = startDestination,
		deepLinks = deepLinks,
		enterTransition = enterTransition,
		exitTransition = exitTransition,
		popEnterTransition = popEnterTransition,
		popExitTransition = popExitTransition,
		builder = builder,
	)
}

/**
 * Construct a nested NavGraph with type-safe arguments and route.
 *
 * Pass the unique destination's serializer as an argument. This is a semi-internal implementation,
 * prefer using the generic function variant.
 *
 * The start destination is passed as a RoutePattern obtained from a relevant Destination instance.
 */
@ExperimentalSerializationApi
@MainThread
public fun <T : Destination> NavGraphBuilder.navigation(
	kClass: KClass<T>,
	serializer: KSerializer<T>,
	startDestination: String,
	deepLinks: List<NavDeepLink> = emptyList(),
	enterTransition: (@JvmSuppressWildcards AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition?)? = null,
	exitTransition: (@JvmSuppressWildcards AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition?)? = null,
	popEnterTransition: (@JvmSuppressWildcards AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition?)? = enterTransition,
	popExitTransition: (@JvmSuppressWildcards AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition?)? = exitTransition,
	builder: NavGraphBuilder.() -> Unit,
) {
	registerDestinationType(kClass, serializer)
	navigation(
		startDestination = startDestination,
		route = createRoutePattern(serializer),
		arguments = createNavArguments(serializer),
		deepLinks = deepLinks,
		enterTransition = enterTransition,
		exitTransition = exitTransition,
		popEnterTransition = popEnterTransition,
		popExitTransition = popExitTransition,
		builder = builder,
	)
}

/**
 * Creates navigation arguments definition based on the Destination type.
 */
@ExperimentalSerializationApi
public fun createNavArguments(serializer: KSerializer<*>): List<NamedNavArgument> =
	List(serializer.descriptor.elementsCount) { i ->
		val name = serializer.descriptor.getElementName(i)
		navArgument(name) {
			// Use StringType for all types to support nullability for all of them.
			type = NavType.StringType
			val isOptional = serializer.descriptor.isNavTypeOptional(i)
			nullable = isOptional
			// If something is optional, the default value is required.
			if (isOptional) {
				defaultValue = null
			}
		}
	}

/**
 * Decodes arguments from a Bundle in NavBackStackEntry instance.
 */
@ExperimentalSerializationApi
public fun <T : Destination> decodeArguments(
	serializer: KSerializer<T>,
	navBackStackEntry: NavBackStackEntry,
): T {
	// Arguments may be empty if the destination does not have any parameters,
	// and it is a start destination.
	val decoder = UriBundleDecoder(navBackStackEntry.arguments ?: Bundle())
	return decoder.decodeSerializableValue(serializer)
}

@Deprecated(
	"Deprecated in favor of navigation builder that supports AnimatedContent",
	level = DeprecationLevel.HIDDEN,
)
@ExperimentalSerializationApi
@MainThread
public fun <T : Destination> NavGraphBuilder.composable(
	kClass: KClass<T>,
	serializer: KSerializer<T>,
	deepLinks: List<NavDeepLink> = emptyList(),
	content: @Composable T.(NavBackStackEntry) -> Unit,
) {
	registerDestinationType(kClass, serializer)
	composable(
		route = createRoutePattern(serializer),
		arguments = createNavArguments(serializer),
		deepLinks = deepLinks,
	) { navBackStackEntry ->
		decodeArguments(serializer, navBackStackEntry).content(navBackStackEntry)
	}
}

@Deprecated(
	"Deprecated in favor of navigation builder that supports AnimatedContent",
	level = DeprecationLevel.HIDDEN,
)
@ExperimentalSerializationApi
@MainThread
public fun <T : Destination> NavGraphBuilder.navigation(
	kClass: KClass<T>,
	serializer: KSerializer<T>,
	startDestination: String,
	deepLinks: List<NavDeepLink> = emptyList(),
	builder: NavGraphBuilder.() -> Unit,
) {
	registerDestinationType(kClass, serializer)
	navigation(
		startDestination = startDestination,
		route = createRoutePattern(serializer),
		arguments = createNavArguments(serializer),
		deepLinks = deepLinks,
		builder = builder,
	)
}
