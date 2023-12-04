package com.kiwi.navigationcompose.typed.internal

import android.os.Bundle
import androidx.lifecycle.SavedStateHandle

internal interface UriDataMap {
	fun contains(key: String): Boolean

	fun get(key: String): String?
}

internal class BundleDataMap(
	private val bundle: Bundle,
) : UriDataMap {
	override fun contains(key: String): Boolean = bundle.containsKey(key)

	override fun get(key: String): String? = bundle.getString(key)
}

internal class SavedStateDataMap(
	private val savedStateHandle: SavedStateHandle,
) : UriDataMap {
	override fun contains(key: String): Boolean = savedStateHandle.contains(key)

	override fun get(key: String): String? = savedStateHandle[key]
}
