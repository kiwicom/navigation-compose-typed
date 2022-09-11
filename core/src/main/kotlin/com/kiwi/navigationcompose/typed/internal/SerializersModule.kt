package com.kiwi.navigationcompose.typed.internal

import androidx.annotation.MainThread
import com.kiwi.navigationcompose.typed.Destination
import kotlinx.serialization.modules.PolymorphicModuleBuilder
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic

private val knownDestinations = mutableListOf<PolymorphicModuleBuilder<Destination>.() -> Unit>()
private var serializersModule: SerializersModule? = null

@MainThread
internal fun addPolymorphicType(
	builder: PolymorphicModuleBuilder<Destination>.() -> Unit,
) {
	knownDestinations.add(builder)
	serializersModule = null
}

@MainThread
internal fun getSerializersModule(): SerializersModule {
	if (serializersModule == null) {
		serializersModule = SerializersModule {
			polymorphic(Destination::class) {
				knownDestinations.forEach { builder ->
					builder()
				}
			}
		}
	}
	return serializersModule!!
}
