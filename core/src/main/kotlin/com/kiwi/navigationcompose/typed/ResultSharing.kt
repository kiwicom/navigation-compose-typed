package com.kiwi.navigationcompose.typed

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.navigation.NavController
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.Json
import kotlinx.serialization.serializer

/**
 * Registers an effect for processing a composable destination's result. See [ResultDestination].
 *
 * Use only to process a result from "full" destination created by `composable` NavGraph builder function.
 * If you are expecting a result from dialog or full-screen dialog, [DialogResultEffect] has to be used
 * to properly handle configuration changes.
 *
 * To avoid specifying generic argument types, use type-inference and type the result argument of the block:
 * ```
 * ComposableResultEffect(navController) { result: Destinations.Dialog.Result ->
 *     // handle result
 * }
 * ```
 */
@Suppress("unused") // T generic parameter is a typecheck for R being the type from ResultDestination
@ExperimentalSerializationApi
@Composable
public inline fun <reified T : ResultDestination<R>, reified R : Any> ComposableResultEffect(
	navController: NavController,
	noinline block: (R) -> Unit,
) {
	// The async waiting workarounds situations when navController.currentBackStackEntry is null
	// ~ not yet populated.
	val currentDestinationState = remember(navController) {
		mutableStateOf(navController.currentDestination)
	}
	val currentDestination = currentDestinationState.value
	if (currentDestination == null) {
		LaunchedEffect(navController) {
			val currentBackStackEntry = navController.currentBackStackEntryFlow.filterNotNull().first()
			currentDestinationState.value = currentBackStackEntry.destination
		}
		return
	}

	ResultEffectImpl(
		navController = navController,
		currentRoute = currentDestination.route!!, // routes are always not null in Nav Compose
		destinationSerializer = serializer<T>(),
		resultSerializer = serializer<R>(),
		block = block,
	)
}

/**
 * Registers an effect for processing a dialog destination's result. See [ResultDestination].
 *
 * Use only to process a result from dialog (and full-screen dialog) destination created by `dialog` NavGraph
 * builder function.
 *
 * To avoid specifying generic argument types, use type-inference and type the result argument of the block:
 * ```
 * DialogResultEffect(
 *     currentRoutePattern = createRoutePattern<Destinations.CurrentDestination>(),
 *     navController = navController,
 * ) { result: Destinations.Dialog.Result ->
 * ```
 */
@Suppress("unused") // T generic parameter is a typecheck for R being the type from ResultDestination
@ExperimentalSerializationApi
@Composable
public inline fun <reified T : ResultDestination<R>, reified R : Any> DialogResultEffect(
	currentRoutePattern: String,
	navController: NavController,
	noinline block: (R) -> Unit,
) {
	ResultEffectImpl(
		navController = navController,
		currentRoute = currentRoutePattern,
		destinationSerializer = serializer<T>(),
		resultSerializer = serializer<R>(),
		block = block,
	)
}

/**
 * Implementation of ResultEffect. Use [ComposableResultEffect] or [DialogResultEffect] directly.
 */
@ExperimentalSerializationApi
@PublishedApi
@Composable
internal fun <T : ResultDestination<R>, R : Any> ResultEffectImpl(
	navController: NavController,
	currentRoute: String,
	destinationSerializer: KSerializer<T>,
	resultSerializer: KSerializer<R>,
	block: (R) -> Unit,
) {
	DisposableEffect(navController) {
		// The implementation is based on the official documentation of the Result sharing.
		// It takes into consideration the possibility of a dialog usage (see the docs).
		// https://developer.android.com/guide/navigation/navigation-programmatic#additional_considerations
		val resultKey = destinationSerializer.descriptor.serialName + "_result"
		val backStackEntry = navController.getBackStackEntry(currentRoute)
		val observer = LifecycleEventObserver { _, event ->
			if (event == Lifecycle.Event.ON_RESUME && backStackEntry.savedStateHandle.contains(resultKey)) {
				val result = backStackEntry.savedStateHandle.remove<String>(resultKey)!!
				val decoded = Json.decodeFromString(resultSerializer, result)
				block(decoded)
			}
		}
		backStackEntry.lifecycle.addObserver(observer)
		onDispose {
			backStackEntry.lifecycle.removeObserver(observer)
		}
	}
}

/**
 * Sets a destination result for the previous backstack entry's destination.
 *
 * The result type has to be KotlinX Serializable.
 */
@ExperimentalSerializationApi
@Suppress("unused") // generic parameter T  is a type-check for R being a ResultDestination's type
public inline fun <reified T : ResultDestination<R>, reified R : Any> NavController.setResult(
	data: R,
) {
	setResultImpl(serializer<T>(), serializer<R>(), data)
}

@ExperimentalSerializationApi
@PublishedApi
internal fun <T : ResultDestination<R>, R : Any> NavController.setResultImpl(
	destinationSerializer: KSerializer<T>,
	serializer: KSerializer<R>,
	data: R,
) {
	// ResultDestination's serializer is used to identify the "key" in the map instead of Result's serializer.
	// This is to avoid issues with polymorphic result types -> setting a result with Result.Success would
	// generate a different key in comparison to just Result type used on the observation side.
	val resultKey = destinationSerializer.descriptor.serialName + "_result"
	val result = Json.encodeToString(serializer, data)
	previousBackStackEntry?.savedStateHandle?.set(resultKey, result)
}
