package com.kiwi.navigationcompose.typed.demo.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.kiwi.navigationcompose.typed.demo.HomeDestinations

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun Demo(
	args: HomeDestinations.Demo,
	onNavigateUp: () -> Unit,
) {
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
		}
	}
}
