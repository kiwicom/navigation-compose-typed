@file:OptIn(ExperimentalMaterial3Api::class)

package com.kiwi.navigationcompose.typed.demo.bottomsheet

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SheetState
import androidx.compose.material3.SheetValue
import androidx.compose.material3.rememberSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.navigation.FloatingWindow
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavDestination
import androidx.navigation.NavOptions
import androidx.navigation.Navigator
import androidx.navigation.NavigatorState
import com.kiwi.navigationcompose.typed.demo.bottomsheet.BottomSheetNavigator.Destination

/**
 * The state of a [ModalBottomSheet] that the [SheetState] drives
 *
 * @param sheetState The sheet state that is driven by the [BottomSheetNavigator]
 */
@Stable
internal class BottomSheetNavigatorSheetState(internal val sheetState: SheetState) {
	/**
	 * @see SheetState.isVisible
	 */
	val isVisible: Boolean
		get() = sheetState.isVisible

	/**
	 * @see SheetState.currentValue
	 */
	val currentValue: SheetValue
		get() = sheetState.currentValue

	/**
	 * @see SheetState.targetValue
	 */
	val targetValue: SheetValue
		get() = sheetState.targetValue
}

/**
 * Create and remember a [BottomSheetNavigator]
 */
@Composable
internal fun rememberBottomSheetNavigator(): BottomSheetNavigator {
	val sheetState = rememberSheetState()
	return remember { BottomSheetNavigator(sheetState) }
}

/**
 * Navigator that drives a [ModalBottomSheetState] for use of [ModalBottomSheetLayout]s
 * with the navigation library. Every destination using this Navigator must set a valid
 * [Composable] by setting it directly on an instantiated [Destination] or calling
 * [androidx.navigation.compose.material.bottomSheet].
 *
 * <b>The [sheetContent] [Composable] will always host the latest entry of the back stack. When
 * navigating from a [BottomSheetNavigator.Destination] to another
 * [BottomSheetNavigator.Destination], the content of the sheet will be replaced instead of a
 * new bottom sheet being shown.</b>
 *
 * When the sheet is dismissed by the user, the [state]'s [NavigatorState.backStack] will be popped.
 *
 * The primary constructor is not intended for public use. Please refer to
 * [rememberBottomSheetNavigator] instead.
 *
 * @param sheetState The [ModalBottomSheetState] that the [BottomSheetNavigator] will use to
 * drive the sheet state
 */
@Navigator.Name("BottomSheetNavigator")
internal class BottomSheetNavigator(
	internal val sheetState: SheetState,
) : Navigator<Destination>() {

	internal val backStack get() = state.backStack

	internal fun dismiss(backStackEntry: NavBackStackEntry) {
		state.popWithTransition(backStackEntry, false)
	}

	override fun navigate(
		entries: List<NavBackStackEntry>,
		navOptions: NavOptions?,
		navigatorExtras: Extras?,
	) {
		entries.forEach { entry ->
			state.push(entry)
		}
	}

	override fun createDestination(): Destination {
		return Destination(this) {}
	}

	override fun popBackStack(popUpTo: NavBackStackEntry, savedState: Boolean) {
		state.popWithTransition(popUpTo, savedState)
	}

	internal fun onTransitionComplete(entry: NavBackStackEntry) {
		state.markTransitionComplete(entry)
	}

	/**
	 * [NavDestination] specific to [BottomSheetNavigator]
	 */
	@NavDestination.ClassType(Composable::class)
	class Destination(
		navigator: BottomSheetNavigator,
		internal val content: @Composable ColumnScope.(NavBackStackEntry) -> Unit,
	) : NavDestination(navigator), FloatingWindow
}
