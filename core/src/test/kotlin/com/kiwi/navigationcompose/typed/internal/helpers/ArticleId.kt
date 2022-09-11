package com.kiwi.navigationcompose.typed.internal.helpers

import kotlinx.serialization.Serializable

@Serializable
@JvmInline
internal value class ArticleId(
	val id: String,
)
