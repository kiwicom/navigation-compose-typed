package com.kiwi.navigationcompose.typed

import androidx.annotation.MainThread
import com.kiwi.navigationcompose.typed.internal.addPolymorphicType
import com.kiwi.navigationcompose.typed.internal.createRouteSlug
import com.kiwi.navigationcompose.typed.internal.isNavTypeOptional
import kotlin.reflect.KClass
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.serializer

/**
 * Registers particular Destination type as polymorphic subclass of [Destination].
 */
@MainThread
public inline fun <reified T : Destination> registerDestinationType() {
	registerDestinationType(T::class, serializer())
}

/**
 * Registers particular Destination type as polymorphic subclass of [Destination].
 *
 * Utilize the reified version of this function.
 */
@MainThread
public fun <T : Destination> registerDestinationType(
	kClass: KClass<T>,
	serializer: KSerializer<T>,
) {
	addPolymorphicType { subclass(kClass, serializer) }
}

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
		if (serializer.descriptor.isNavTypeOptional(i)) {
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
