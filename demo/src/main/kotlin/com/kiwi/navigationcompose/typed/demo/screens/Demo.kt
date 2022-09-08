package com.kiwi.navigationcompose.typed.demo

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
internal fun Demo(
	args: Destinations.Demo,
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
