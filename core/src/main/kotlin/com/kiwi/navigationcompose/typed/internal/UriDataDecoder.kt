package com.kiwi.navigationcompose.typed.internal

import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.AbstractDecoder
import kotlinx.serialization.encoding.CompositeDecoder
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule

@ExperimentalSerializationApi
internal class UriDataDecoder(
	private val data: UriDataMap,
) : AbstractDecoder() {
	override val serializersModule: SerializersModule by lazy { getSerializersModule() }

	private val json by lazy {
		Json { serializersModule = this@UriDataDecoder.serializersModule }
	}

	private var root = true
	private var elementsCount = 0
	private var elementIndex = 0
	private var elementName = ""

	override fun <T> decodeSerializableValue(deserializer: DeserializationStrategy<T>): T {
		return if (root || deserializer.descriptor.kind.isNativelySupported()) {
			super.decodeSerializableValue(deserializer)
		} else {
			json.decodeFromString(deserializer, decodeString())
		}
	}

	override fun decodeElementIndex(descriptor: SerialDescriptor): Int {
		while (elementIndex < elementsCount) {
			elementName = descriptor.getElementName(elementIndex)
			elementIndex++
			if (data.contains(elementName)) {
				return elementIndex - 1
			}
		}
		return CompositeDecoder.DECODE_DONE
	}

	override fun decodeNotNullMark(): Boolean =
		data.contains(elementName) && data.get(elementName) != null

	override fun decodeNull(): Nothing? = null

	// natively supported

	override fun decodeInt(): Int = data.get(elementName)!!.toInt()

	override fun decodeLong(): Long = data.get(elementName)!!.toLong()

	override fun decodeFloat(): Float = data.get(elementName)!!.toFloat()

	override fun decodeBoolean(): Boolean = data.get(elementName)!!.toBooleanStrict()

	override fun decodeString(): String = data.get(elementName)!!

	// delegated to other primitives

	override fun decodeDouble(): Double = data.get(elementName)!!.toDouble()

	override fun decodeByte(): Byte = data.get(elementName)!!.toByte()

	override fun decodeShort(): Short = data.get(elementName)!!.toShort()

	override fun decodeChar(): Char = data.get(elementName)!![0]

	override fun decodeEnum(enumDescriptor: SerialDescriptor): Int = decodeInt()

	override fun beginStructure(descriptor: SerialDescriptor): CompositeDecoder {
		elementsCount = descriptor.elementsCount
		root = false
		return this
	}
}
