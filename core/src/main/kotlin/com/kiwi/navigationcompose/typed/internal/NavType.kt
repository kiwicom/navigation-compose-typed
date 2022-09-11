package com.kiwi.navigationcompose.typed.internal

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.SerialKind

@ExperimentalSerializationApi
internal fun SerialKind.isNativelySupported(): Boolean =
	this == PrimitiveKind.INT ||
		this == PrimitiveKind.FLOAT ||
		this == PrimitiveKind.LONG ||
		this == PrimitiveKind.BOOLEAN ||
		this == PrimitiveKind.STRING ||
		this == PrimitiveKind.DOUBLE ||
		this == PrimitiveKind.BYTE ||
		this == PrimitiveKind.SHORT ||
		this == PrimitiveKind.CHAR ||
		this == SerialKind.ENUM

/**
 * Optional parameter is if:
 * - there is a default value for the property
 * - property is nullable -> it has to be modelled as a missing query parameter
 * - property is a String that can be empty -> it has to be modelled as a query parameter
 */
@ExperimentalSerializationApi
internal fun SerialDescriptor.isNavTypeOptional(index: Int): Boolean =
	isElementOptional(index) ||
		getElementDescriptor(index).let {
			it.isNullable || it.kind == PrimitiveKind.STRING
		}
