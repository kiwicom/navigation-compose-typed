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

		val url = Uri.Builder()
		val encoder = UrlEncoder(url)
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

		val expectedUrl = Uri.Builder().apply {
			appendPath("1")
			appendPath("2.23")
			appendPath("3")
			appendPath("true")
			appendQueryParameter("e", "value")
			appendPath("4.0")
			appendPath("5")
			appendPath("6")
			appendPath("7")
			appendPath("[8,9]")
			appendPath("""{"10":"11"}""")
			appendPath("""{"int":12}""")
			appendPath("{}")
			appendPath("""{"type":"com.kiwi.navigationcompose.typed.internal.helpers.SubSealed.B","int":13}""")
		}
		Assert.assertEquals(expectedUrl.toString(), url.toString())
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

		val url = Uri.Builder()
		val encoder = UrlEncoder(url)
		val testData = TestData()

		encoder.encodeSerializableValue(serializer(), testData)

		val expectedUrl = Uri.Builder().apply {
			appendQueryParameter("a", "1")
			appendQueryParameter("c", "3")
			appendQueryParameter("e", "value")
			appendQueryParameter("g", "5")
			appendQueryParameter("i", "7")
			appendQueryParameter("k", """{"10":"11"}""")
			appendQueryParameter("m", "{}")
		}
		Assert.assertEquals(expectedUrl.toString(), url.toString())
	}
}
