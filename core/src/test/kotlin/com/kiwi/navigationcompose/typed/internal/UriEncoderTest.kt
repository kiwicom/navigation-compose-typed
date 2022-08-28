package com.kiwi.navigationcompose.typed.internal

import android.net.Uri
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
internal class UriEncoderTest {
	@Test
	fun testValues() {
		@Suppress("unused")
		@Serializable
		class TestData(
			val a: Int,
			val b: Float,
			val c: Long,
			val d: Boolean,
			val e: String,
			val f: Double,
			val g: Byte,
			val h: Char,
			val i: Short,
			val j: List<Int>,
			val k: Map<Int, String>,
			val l: SubClass,
			val m: SubObject,
			val n: SubSealed,
		)

		val uri = Uri.Builder()
		val encoder = UriEncoder(uri)
		val testData = TestData(
			a = 1,
			b = 2.23f,
			c = 3L,
			d = true,
			e = "value",
			f = 4.0,
			g = 5.toByte(),
			h = '6',
			i = 7,
			j = listOf(8, 9),
			k = mapOf(10 to "11"),
			l = SubClass(12),
			m = SubObject,
			n = SubSealed.B(13),
		)

		encoder.encodeSerializableValue(serializer(), testData)

		val expectedJ = Uri.encode("[8,9]")
		val expectedK = Uri.encode("""{"10":"11"}""")
		val expectedL = Uri.encode("""{"int":12}""")
		val expectedM = Uri.encode("{}")
		val expectedN = Uri.encode(
			"""{"type":"com.kiwi.navigationcompose.typed.internal.helpers.SubSealed.B","int":13}""",
		)
		Assert.assertEquals(
			"/1/2.23/3/true/value/4.0/5/6/7/$expectedJ/$expectedK/$expectedL/$expectedM/$expectedN",
			uri.build().toString(),
		)
	}

	@Test
	fun testNullability() {
		@Suppress("unused")
		@Serializable
		class TestData(
			val a: Int? = 1,
			val b: Float? = null,
			val c: Long? = 3L,
			val d: Boolean? = null,
			val e: String? = "value",
			val f: Double? = null,
			val g: Byte? = 5.toByte(),
			val h: Char? = null,
			val i: Short? = 7,
			val j: List<Int>? = null,
			val k: Map<Int, String>? = mapOf(10 to "11"),
			val l: SubClass? = null,
			val m: SubObject? = SubObject,
			val n: SubSealed? = null,
		)

		val uri = Uri.Builder()
		val encoder = UriEncoder(uri)
		val testData = TestData()

		encoder.encodeSerializableValue(serializer(), testData)

		val expectedK = Uri.encode("""{"10":"11"}""")
		val expectedM = Uri.encode("{}")
		Assert.assertEquals(
			"?a=1&c=3&e=value&g=5&i=7&k=$expectedK&m=$expectedM",
			uri.build().toString(),
		)
	}
}
