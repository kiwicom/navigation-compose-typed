package com.kiwi.navigationcompose.typed.internal

import com.kiwi.navigationcompose.typed.Destination
import com.kiwi.navigationcompose.typed.createRoutePattern
import com.kiwi.navigationcompose.typed.internal.helpers.SubClass
import com.kiwi.navigationcompose.typed.internal.helpers.SubObject
import com.kiwi.navigationcompose.typed.internal.helpers.SubSealed
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import org.junit.Assert
import org.junit.Test

@OptIn(ExperimentalSerializationApi::class)
internal class RoutePatternTest {
	@Test
	fun test() {
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
			val j: List<Int> = listOf(8, 9),
			val k: Map<Int, String>,
			val l: SubClass = SubClass(12),
			val m: SubObject,
			val n: SubSealed = SubSealed.B(13),
		) : Destination

		Assert.assertEquals(
			"com.kiwi.navigationcompose.typed.internal.RoutePatternTest.test.TestData/{a}/{c}/{e}/{g}/{i}/{k}/{m}?b={b}&d={d}&f={f}&h={h}&j={j}&l={l}&n={n}",
			createRoutePattern<TestData>(),
		)
	}
}
