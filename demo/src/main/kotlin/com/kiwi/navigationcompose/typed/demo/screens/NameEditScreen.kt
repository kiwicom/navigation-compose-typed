package com.kiwi.navigationcompose.typed.demo.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.navigation.NavController
import com.kiwi.navigationcompose.typed.demo.ProfileDestinations
import com.kiwi.navigationcompose.typed.setResult
import kotlinx.serialization.ExperimentalSerializationApi

@ExperimentalSerializationApi
@Composable
internal fun NameEditScreen(navController: NavController) {
	NameEdit(
		onNameSave = { name ->
			navController.setResult(ProfileDestinations.NameEditScreen.Result(name))
			navController.navigateUp()
		},
	)
}

@Composable
private fun NameEdit(
	onNameSave: (String) -> Unit,
) {
	var name by rememberSaveable { mutableStateOf("") }
	Column {
		Text("Set your name")
		OutlinedTextField(
			value = name,
			onValueChange = { name = it },
		)
		TextButton(onClick = { onNameSave(name) }) {
			Text("Save")
		}
	}
}
