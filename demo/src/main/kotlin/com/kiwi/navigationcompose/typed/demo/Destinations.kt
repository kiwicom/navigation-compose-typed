package com.kiwi.navigationcompose.typed.demo

import com.kiwi.navigationcompose.typed.Destination
import com.kiwi.navigationcompose.typed.ResultDestination
import kotlinx.serialization.Serializable

internal sealed interface Destinations : Destination {
	@Serializable
	data object Home : Destinations

	@Serializable
	data object List : Destinations

	@Serializable
	data object Profile : Destinations
}

internal sealed interface HomeDestinations : Destination {
	@Serializable
	data object Home : HomeDestinations

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
	data object Home : ProfileDestinations

	@Serializable
	data object NameEditDialog : ProfileDestinations, ResultDestination<NameEditDialog.Result> {
		@Serializable
		data class Result(val name: String)
	}

	@Serializable
	data object NameEditScreen : ProfileDestinations, ResultDestination<NameEditScreen.Result> {
		@Serializable
		sealed interface Result {
			@Serializable
			data class Success(val name: String) : Result

			@Serializable
			data object Cancelled : Result
		}
	}
}
