package com.kiwi.navigationcompose.typed

/**
 * Instance of this interface represents a destination including its arguments.
 *
 * Each destination has to be a KotlinX-Serializable type.
 *
 * Utilize sealed interface to define a set of known destinations:
 *
 * ```
 * sealed interface Destinations : Destination {
 *      @Serializable
 *      object Home : Destinations
 *
 *      @Serializable
 *      data class Article(val id: Int) : Destinations
 * }
 * ```
 */
public interface Destination
