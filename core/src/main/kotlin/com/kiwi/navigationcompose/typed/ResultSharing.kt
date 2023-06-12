package com.kiwi.navigationcompose.typed

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.navigation.NavController
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
public inline fun <T : ResultDestination<R>, reified R : Any> ComposableResultEffect(
	navController: NavController,
	noinline block: (R) -> Unit,
) {
	ResultEffectImpl(
		navController = navController,
		currentRoute = navController.currentDestination!!.route!!,
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
public inline fun <T : ResultDestination<R>, reified R : Any> DialogResultEffect(
	currentRoutePattern: String,
	navController: NavController,
	noinline block: (R) -> Unit,
) {
	ResultEffectImpl(
		navController = navController,
		currentRoute = currentRoutePattern,
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
internal fun <R : Any> ResultEffectImpl(
	navController: NavController,
	currentRoute: String,
	resultSerializer: KSerializer<R>,
	block: (R) -> Unit,
) {
	DisposableEffect(navController) {
		// The implementation is based on the official documentation of the Result sharing.
		// It takes into consideration the possibility of a dialog usage (see the docs).
		// https://developer.android.com/guide/navigation/navigation-programmatic#additional_considerations
		val resultKey = resultSerializer.descriptor.serialName + "_result"
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
public inline fun <T : ResultDestination<R>, reified R : Any> NavController.setResult(
	data: R,
) {
	setResultImpl(serializer(), data)
}

@ExperimentalSerializationApi
@PublishedApi
internal fun <R : Any> NavController.setResultImpl(
	serializer: KSerializer<R>,
	data: R,
) {
	val result = Json.encodeToString(serializer, data)
	val resultKey = serializer.descriptor.serialName + "_result"
	previousBackStackEntry?.savedStateHandle?.set(resultKey, result)
}
