package com.kiwi.navigationcompose.typed.demo

import com.kiwi.navigationcompose.typed.Destination
import kotlinx.serialization.Serializable

internal sealed interface Destinations : Destination {
	@Serializable
	object Home : Destinations

	@Serializable
	data class Demo(
		val int: Int,
		val intNullable: Int?,
		val intOptional: Int = 1,
		val intNullableOptional: Int? = null,
		val string: String,
		val stringNullable: String?,
		val stringOptional: String = "default",
		val stringNullableOptional: String? = null,
	) : Destinations
}
