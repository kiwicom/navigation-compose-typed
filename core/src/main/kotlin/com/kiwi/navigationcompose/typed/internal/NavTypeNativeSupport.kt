package com.kiwi.navigationcompose.typed.internal

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.descriptors.PrimitiveKind
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
