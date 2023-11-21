package com.kiwi.navigationcompose.typed.demo.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.viewModelFactory
import com.kiwi.navigationcompose.typed.decodeArguments
import com.kiwi.navigationcompose.typed.demo.HomeDestinations
import kotlinx.serialization.ExperimentalSerializationApi

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun Demo(
	args: HomeDestinations.Demo,
	onNavigateUp: () -> Unit,
) {
	val viewModel = viewModel<StateDemoViewModel>(factory = ViewModelFactory)
	Scaffold(
		topBar = {
			TopAppBar(
				title = { Text("Demo") },
				navigationIcon = {
					IconButton(onClick = onNavigateUp) {
						Icon(Icons.Default.ArrowBack, contentDescription = null)
					}
				},
			)
		},
	) {
		Column(
			Modifier
				.padding(it)
				.fillMaxSize()
				.wrapContentSize(),
		) {
			Text(
				text = args.toString(),
			)
			Divider()
			Text(
				text = viewModel.arguments.toString(),
			)
		}
	}
}

internal val ViewModelFactory = viewModelFactory {
	addInitializer(StateDemoViewModel::class) { StateDemoViewModel(createSavedStateHandle()) }
}

internal class StateDemoViewModel(
	state: SavedStateHandle,
) : ViewModel() {
	@OptIn(ExperimentalSerializationApi::class)
	val arguments = state.decodeArguments<HomeDestinations.Demo>()
}
