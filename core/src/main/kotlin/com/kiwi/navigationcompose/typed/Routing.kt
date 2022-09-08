package com.kiwi.navigationcompose.typed

import com.kiwi.navigationcompose.typed.internal.UrlEncoder
import com.kiwi.navigationcompose.typed.internal.createRouteSlug
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.serializer
import okhttp3.HttpUrl

/**
 * Converts a Destination to route pattern.
 *
 * Use the route pattern for startDestination argument when using a NavGraph/navigation composables.
 *
 * ```
 * NavGraph(
 *    startDestination = createRoutePattern<Destinations.Home>(),
 * ) {
 * }
 * ```
 */
@ExperimentalSerializationApi
public inline fun <reified T : Destination> createRoutePattern(): String =
	createRoutePattern(serializer<T>())

/**
 * Converts a Destination to route pattern.
 *
 * Utilize the generic variant of this function.
 */
@ExperimentalSerializationApi
public fun <T : Destination> createRoutePattern(serializer: KSerializer<T>): String {
	val destination = createRouteSlug(serializer)
	if (serializer.descriptor.elementsCount == 0) {
		return destination
	}

	val path = StringBuilder()
	val query = StringBuilder()
	for (i in 0 until serializer.descriptor.elementsCount) {
		val name = serializer.descriptor.getElementName(i)
		if (serializer.descriptor.isElementOptional(i) || serializer.descriptor.getElementDescriptor(i).isNullable) {
			query.append("&$name={$name}")
		} else {
			path.append("/{$name}")
		}
	}
	if (query.isNotEmpty()) {
		query[0] = '?'
	}

	return destination + path.toString() + query.toString()
}

/**
 * Converts a Destination to Route.
 *
 * This conversion is inlined, the specific generic T argument has to be "known", i.e. this conversion
 * cannot happen later in the flow with T being just a Destination. Covert to Route at the same time
 * when you are creating the destination instance.
 *
 * ```
 * class HomeViewModel : ViewModel() {
 *     fun onArticleClick(id: Int) {
 *         navEvents.tryEmit(Destinations.Article(id).toRoute())
 *     }
 * }
 * ```
 */
@ExperimentalSerializationApi
public inline fun <reified T : Destination> T.toRoute(): Route =
	createRoute(serializer())

/**
 * Converts a Destination to Route.
 *
 * Utilize the generic variant of this function.
 */
@ExperimentalSerializationApi
public fun <T : Destination> T.createRoute(serializer: KSerializer<T>): Route {
	val urlBuilder = HttpUrl.Builder().apply {
		scheme("https")
		host(createRouteSlug(serializer))
	}
	val encoder = UrlEncoder(urlBuilder)
	encoder.encodeSerializableValue(serializer, this)
	return Route(urlBuilder.build().toString().substring(8))
}
