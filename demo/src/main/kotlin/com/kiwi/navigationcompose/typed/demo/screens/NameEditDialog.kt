package com.kiwi.navigationcompose.typed.demo.screens

import androidx.compose.material.AlertDialog
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
internal fun NameEditDialog(navController: NavController) {
	NameEdit(
		onNameSave = { name ->
			navController.setResult(ProfileDestinations.NameEditDialog.Result(name))
			navController.navigateUp()
		},
		onDismiss = navController::navigateUp,
	)
}

@Composable
private fun NameEdit(
	onNameSave: (String) -> Unit,
	onDismiss: () -> Unit,
) {
	var name by rememberSaveable { mutableStateOf("") }
	AlertDialog(
		onDismissRequest = onDismiss,
		title = { Text("Set your name") },
		text = {
			OutlinedTextField(
				value = name,
				onValueChange = { name = it },
			)
		},
		confirmButton = {
			TextButton(onClick = { onNameSave(name) }) {
				Text("Save")
			}
		},
	)
}
