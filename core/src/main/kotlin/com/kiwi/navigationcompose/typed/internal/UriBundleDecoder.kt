package com.kiwi.navigationcompose.typed.internal

import android.os.Bundle
import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.AbstractDecoder
import kotlinx.serialization.encoding.CompositeDecoder
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.EmptySerializersModule
import kotlinx.serialization.modules.SerializersModule

@ExperimentalSerializationApi
internal class UriBundleDecoder(
	private val bundle: Bundle,
) : AbstractDecoder() {
	override val serializersModule: SerializersModule = EmptySerializersModule()

	private var root = true
	private var elementsCount = 0
	private var elementIndex = 0
	private var elementName = ""

	override fun <T> decodeSerializableValue(deserializer: DeserializationStrategy<T>): T {
		return if (root || deserializer.descriptor.kind.isNativelySupported()) {
			super.decodeSerializableValue(deserializer)
		} else {
			Json.decodeFromString(deserializer, decodeString())
		}
	}

	override fun decodeElementIndex(descriptor: SerialDescriptor): Int {
		while (elementIndex < elementsCount) {
			elementName = descriptor.getElementName(elementIndex)
			elementIndex++
			if (bundle.containsKey(elementName)) {
				return elementIndex - 1
			}
		}
		return CompositeDecoder.DECODE_DONE
	}

	override fun decodeNotNullMark(): Boolean =
		bundle.containsKey(elementName) && bundle.getString(elementName) != null

	override fun decodeNull(): Nothing? = null

	// natively supported

	override fun decodeInt(): Int = bundle.getString(elementName)!!.toInt()
	override fun decodeLong(): Long = bundle.getString(elementName)!!.toLong()
	override fun decodeFloat(): Float = bundle.getString(elementName)!!.toFloat()
	override fun decodeBoolean(): Boolean = bundle.getString(elementName)!!.toBooleanStrict()
	override fun decodeString(): String = bundle.getString(elementName)!!

	// delegated to other primitives

	override fun decodeDouble(): Double = bundle.getString(elementName)!!.toDouble()
	override fun decodeByte(): Byte = bundle.getString(elementName)!!.toByte()
	override fun decodeShort(): Short = bundle.getString(elementName)!!.toShort()
	override fun decodeChar(): Char = bundle.getString(elementName)!![0]

	override fun decodeEnum(enumDescriptor: SerialDescriptor): Int = decodeInt()

	override fun beginStructure(descriptor: SerialDescriptor): CompositeDecoder {
		elementsCount = descriptor.elementsCount
		root = false
		return this
	}
}
