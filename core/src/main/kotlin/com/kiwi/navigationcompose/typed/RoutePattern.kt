package com.kiwi.navigationcompose.typed

/**
 * Represents destination URL pattern.
 *
 * For type-safety, it is modelled as value class.
 */
@JvmInline
public value class RoutePattern internal constructor(
	public val pattern: String,
)
