package com.kiwi.navigationcompose.typed.internal

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer

@ExperimentalSerializationApi
internal fun createRouteSlug(serializer: KSerializer<*>): String =
	serializer.descriptor.serialName
