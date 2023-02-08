package com.kiwi.navigationcompose.typed.demo.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.kiwi.navigationcompose.typed.ComposableResultEffect
import com.kiwi.navigationcompose.typed.Destination
import com.kiwi.navigationcompose.typed.DialogResultEffect
import com.kiwi.navigationcompose.typed.createRoutePattern
import com.kiwi.navigationcompose.typed.demo.ProfileDestinations
import com.kiwi.navigationcompose.typed.navigate
import kotlinx.serialization.ExperimentalSerializationApi

@ExperimentalSerializationApi
@Composable
internal fun Profile(navController: NavController) {
	var name by rememberSaveable { mutableStateOf("") }
	var times by rememberSaveable { mutableStateOf(0) }

	DialogResultEffect(
		currentRoutePattern = createRoutePattern<ProfileDestinations.Home>(),
		navController = navController,
	) { result: ProfileDestinations.NameEditDialog.Result ->
		name = "Dialog: ${result.name}"
		times += 1
	}
	ComposableResultEffect(navController) { result: ProfileDestinations.NameEditScreen.Result ->
		name = "Screen: ${result.name}"
		times += 1
	}

	Profile(name, times, navController::navigate)
}

@Composable
private fun Profile(
	name: String,
	times: Int,
	onNavigate: (Destination) -> Unit,
) {
	Column(
		Modifier
			.fillMaxSize()
			.wrapContentSize(),
		horizontalAlignment = Alignment.CenterHorizontally,
	) {
		Text("Profile")

		Text("Name: $name, changed: $times")

		OutlinedButton(
			onClick = { onNavigate(ProfileDestinations.NameEditDialog) },
		) {
			Text("Change Name Dialog")
		}
		OutlinedButton(
			onClick = { onNavigate(ProfileDestinations.NameEditScreen) },
		) {
			Text("Change Name Screen")
		}
	}
}
