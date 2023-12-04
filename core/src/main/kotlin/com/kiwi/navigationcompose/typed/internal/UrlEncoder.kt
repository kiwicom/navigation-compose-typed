package com.kiwi.navigationcompose.typed.internal

import android.net.Uri
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerializationStrategy
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.AbstractEncoder
import kotlinx.serialization.encoding.CompositeEncoder
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule

@ExperimentalSerializationApi
internal class UrlEncoder(
	private val url: Uri.Builder,
) : AbstractEncoder() {
	override val serializersModule: SerializersModule by lazy { getSerializersModule() }

	private val json by lazy {
		Json { serializersModule = this@UrlEncoder.serializersModule }
	}

	private var root = true
	private var elementName = ""
	private var elementOptional = false

	override fun <T> encodeSerializableValue(serializer: SerializationStrategy<T>, value: T) {
		if (root || serializer.descriptor.kind.isNativelySupported()) {
			super.encodeSerializableValue(serializer, value)
		} else {
			encodeString(json.encodeToString(serializer, value))
		}
	}

	override fun encodeElement(descriptor: SerialDescriptor, index: Int): Boolean {
		elementName = descriptor.getElementName(index)
		elementOptional = descriptor.isNavTypeOptional(index)
		return true
	}

	override fun encodeNull() {}

	override fun encodeNotNullMark() {}

	override fun encodeInt(value: Int) = appendValue(value.toString())

	override fun encodeLong(value: Long) = appendValue(value.toString())

	override fun encodeFloat(value: Float) = appendValue(value.toString())

	override fun encodeBoolean(value: Boolean) = appendValue(if (value) "true" else "false")

	override fun encodeString(value: String) = appendValue(value)

	override fun encodeDouble(value: Double) = appendValue(value.toString())

	override fun encodeByte(value: Byte) = appendValue(value.toString())

	override fun encodeShort(value: Short) = appendValue(value.toString())

	override fun encodeChar(value: Char) = appendValue(value.toString())

	override fun encodeEnum(enumDescriptor: SerialDescriptor, index: Int) = encodeInt(index)

	override fun beginStructure(descriptor: SerialDescriptor): CompositeEncoder {
		// only the Destination top level -> ignore nesting in this case
		root = false
		return this
	}

	private fun appendValue(value: String) {
		if (elementOptional) {
			url.appendQueryParameter(elementName, value)
		} else {
			url.appendPath(value)
		}
	}
}
