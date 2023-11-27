package com.kiwi.navigationcompose.typed.internal

import androidx.core.os.bundleOf
import com.kiwi.navigationcompose.typed.Destination
import com.kiwi.navigationcompose.typed.internal.helpers.ArticleId
import com.kiwi.navigationcompose.typed.internal.helpers.SubClass
import com.kiwi.navigationcompose.typed.internal.helpers.SubObject
import com.kiwi.navigationcompose.typed.internal.helpers.SubSealed
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.serializer
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@OptIn(ExperimentalSerializationApi::class)
@RunWith(RobolectricTestRunner::class)
internal class UriDataDecoderTest {
	@Suppress("unused")
	@Serializable
	class TestData(
		val a: Int,
		val b: Float = 2.23f,
		val c: Long,
		val d: Boolean = true,
		val e: String,
		val f: Double = 4.0,
		val g: Byte,
		val h: Char = '6',
		val i: Short,
		val j: List<Int>,
		val k: Map<Int, String>,
		val l: SubClass,
		val m: SubObject,
		val n: SubSealed,
		val o: ArticleId,
	) : Destination

	@Test
	fun testOptional() {
		val bundle = bundleOf(
			"a" to "1",
			"c" to "3",
			"e" to "test",
			"g" to "5",
			"i" to "7",
			"j" to "[8,9]",
			"k" to """{"10":"11"}""",
			"l" to """{"int":12}""",
			"m" to "{}",
			"n" to """{"type":"com.kiwi.navigationcompose.typed.internal.helpers.SubSealed.B","int":13}""",
			"o" to "\"14\"",
		)

		val decoder = UriDataDecoder(BundleDataMap(bundle))
		val data = decoder.decodeSerializableValue(serializer<TestData>())

		Assert.assertEquals(1, data.a)
		Assert.assertEquals(2.23f, data.b)
		Assert.assertEquals(3L, data.c)
		Assert.assertEquals(true, data.d)
		Assert.assertEquals("test", data.e)
		Assert.assertEquals(4.0, data.f, 0.0)
		Assert.assertEquals(5.toByte(), data.g)
		Assert.assertEquals('6', data.h)
		Assert.assertEquals(7.toShort(), data.i)
		Assert.assertEquals(listOf(8, 9), data.j)
		Assert.assertEquals(mapOf(10 to "11"), data.k)
		Assert.assertEquals(12, data.l.int)
		Assert.assertEquals(SubObject, data.m)
		Assert.assertEquals(13, (data.n as? SubSealed.B)?.int)
		Assert.assertEquals(ArticleId("14"), data.o)
	}
}
