package com.kiwi.navigationcompose.typed.demo.bottomsheet

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveableStateHolder
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.navigation.NavBackStackEntry
import androidx.navigation.compose.LocalOwnersProvider

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun BottomSheetHost(
	bottomSheetNavigator: BottomSheetNavigator,
) {
	val saveableStateHolder = rememberSaveableStateHolder()
	val dialogBackStack by bottomSheetNavigator.backStack.collectAsState()
	val visibleBackStack = rememberVisibleList(dialogBackStack)

	visibleBackStack.PopulateVisibleList(dialogBackStack)

	visibleBackStack.forEach { backStackEntry ->
		val destination = backStackEntry.destination as BottomSheetNavigator.Destination
		ModalBottomSheet(
			onDismissRequest = { bottomSheetNavigator.dismiss(backStackEntry) },
			sheetState = bottomSheetNavigator.sheetState
		) {
			DisposableEffect(backStackEntry) {
				onDispose {
					bottomSheetNavigator.onTransitionComplete(backStackEntry)
				}
			}

			// while in the scope of the composable, we provide the navBackStackEntry as the
			// ViewModelStoreOwner and LifecycleOwner
			backStackEntry.LocalOwnersProvider(saveableStateHolder) {
				Column {
					destination.content(this, backStackEntry)
				}
			}
		}
	}
}

@Composable
internal fun MutableList<NavBackStackEntry>.PopulateVisibleList(
	transitionsInProgress: Collection<NavBackStackEntry>
) {
	transitionsInProgress.forEach { entry ->
		DisposableEffect(entry.getLifecycle()) {
			val observer = LifecycleEventObserver { _, event ->
				// ON_START -> add to visibleBackStack, ON_STOP -> remove from visibleBackStack
				if (event == Lifecycle.Event.ON_START) {
					// We want to treat the visible lists as Sets but we want to keep
					// the functionality of mutableStateListOf() so that we recompose in response
					// to adds and removes.
					if (!contains(entry)) {
						add(entry)
					}
				}
				if (event == Lifecycle.Event.ON_STOP) {
					remove(entry)
				}
			}
			entry.getLifecycle().addObserver(observer)
			onDispose {
				entry.getLifecycle().removeObserver(observer)
			}
		}
	}
}

@Composable
internal fun rememberVisibleList(transitionsInProgress: Collection<NavBackStackEntry>) =
	remember(transitionsInProgress) {
		mutableStateListOf<NavBackStackEntry>().also {
			it.addAll(
				transitionsInProgress.filter { entry ->
					entry.getLifecycle().currentState.isAtLeast(Lifecycle.State.STARTED)
				}
			)
		}
	}
