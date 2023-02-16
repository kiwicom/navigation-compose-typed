package com.kiwi.navigationcompose.typed.demo

import com.kiwi.navigationcompose.typed.Destination
import com.kiwi.navigationcompose.typed.ResultDestination
import kotlinx.serialization.Serializable

internal sealed interface Destinations : Destination {
	@Serializable
	object Home : Destinations

	@Serializable
	object List : Destinations

	@Serializable
	object Profile : Destinations
}

internal sealed interface HomeDestinations : Destination {
	@Serializable
	object Home : HomeDestinations

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
	) : HomeDestinations
}

internal sealed interface ProfileDestinations : Destination {
	@Serializable
	object Home : ProfileDestinations

	@Serializable
	object NameEditDialog : ProfileDestinations, ResultDestination<NameEditDialog.Result> {
		@Serializable
		data class Result(val name: String)
	}

	@Serializable
	object NameEditBottomSheetDialog : ProfileDestinations, ResultDestination<NameEditBottomSheetDialog.Result> {
		@Serializable
		data class Result(val name: String)
	}

	@Serializable
	object NameEditScreen : ProfileDestinations, ResultDestination<NameEditScreen.Result> {
		@Serializable
		data class Result(val name: String)
	}
}
