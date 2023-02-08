package com.kiwi.navigationcompose.typed.demo.screens

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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

@OptIn(ExperimentalMaterial3Api::class)
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
