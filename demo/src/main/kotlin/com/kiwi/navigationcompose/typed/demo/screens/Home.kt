package com.kiwi.navigationcompose.typed.demo

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.kiwi.navigationcompose.typed.Route
import com.kiwi.navigationcompose.typed.toRoute
import kotlinx.serialization.ExperimentalSerializationApi

@ExperimentalSerializationApi
@Composable
internal fun Home(
	onNavigate: (Route) -> Unit,
) {
	Column(
		Modifier
			.fillMaxSize()
			.wrapContentSize(),
	) {
		OutlinedButton(
			onClick = {
				onNavigate(
					Destinations.Demo(
						int = 1,
						intNullable = null,
						string = "test",
						stringNullable = null,
					).toRoute(),
				)
			},
		) {
			Text("Demo 1")
		}

		OutlinedButton(
			onClick = {
				onNavigate(
					Destinations.Demo(
						int = 1,
						intNullable = 2,
						intOptional = 3,
						intNullableOptional = 4,
						string = "test",
						stringNullable = "test2",
						stringOptional = "test3",
						stringNullableOptional = "test4",
					).toRoute(),
				)
			},
		) {
			Text("Demo 2")
		}

		OutlinedButton(
			onClick = {
				onNavigate(
					Destinations.Demo(
						int = 1,
						intNullable = 2,
						intOptional = 3,
						intNullableOptional = 4,
						string = "",
						stringNullable = "",
						stringOptional = "",
						stringNullableOptional = "",
					).toRoute(),
				)
			},
		) {
			Text("Demo 3")
		}
	}
}
