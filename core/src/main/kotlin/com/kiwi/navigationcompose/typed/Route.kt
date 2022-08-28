package com.kiwi.navigationcompose.typed

/**
 * Represents resulting destination URL.
 *
 * For type-safety, it is modelled as value class.
 */
@JvmInline
public value class Route internal constructor(
	public val url: String,
)
