package com.kiwi.navigationcompose.typed.internal.helpers

import kotlinx.serialization.Serializable

@Serializable
internal sealed class SubSealed {
	@Suppress("unused")
	@Serializable
	data class A(val int: Int) : SubSealed()

	@Serializable
	data class B(val int: Int) : SubSealed()
}
