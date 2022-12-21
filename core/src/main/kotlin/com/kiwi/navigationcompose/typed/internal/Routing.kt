package com.kiwi.navigationcompose.typed.internal

import android.net.Uri
import androidx.annotation.MainThread
import com.kiwi.navigationcompose.typed.Destination
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerializationStrategy

@ExperimentalSerializationApi
internal fun createRouteSlug(serializer: KSerializer<*>): String =
	serializer.descriptor.serialName

@ExperimentalSerializationApi
internal fun createRouteSlug(serializationStrategy: SerializationStrategy<*>): String =
	serializationStrategy.descriptor.serialName

/**
 * Converts a Destination to Route.
 *
 * This conversion is inlined. The specific generic T argument has to be "known", i.e. this conversion
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
@MainThread
internal fun <T : Destination> T.toRoute(): String {
	val serializer = requireNotNull(
		getSerializersModule().getPolymorphic(Destination::class, this),
	) {
		"Polymorphic serializer for $this is not registered. Use registerDestinationType() function."
	}
	val urlBuilder = Uri.Builder().apply {
		appendPath(createRouteSlug(serializer))
	}
	val encoder = UrlEncoder(urlBuilder)
	encoder.encodeSerializableValue(serializer, this)
	return urlBuilder.build().toString().substring(1)
}
