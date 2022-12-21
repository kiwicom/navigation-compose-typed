package com.kiwi.navigationcompose.typed

/**
 * Marks the destinations as destination being able to return a result.
 *
 * The result type is defined as the generic argument if this interface and has to be KotlinX Serializable.
 *
 * Use [ComposableResultEffect] or [DialogResultEffect] to observe the result.
 * Use [setResult] to send the result from a destination.
 *
 * ```
 * sealed interface Destinations : Destination {
 *     // ...
 *     @Serializable
 * 	   object Dialog : Destinations, ResultDestination<Dialog.Result> {
 * 	       @Serializable
 *         data class Result(val name: String)
 *     }
 * }
 * ```
 *
 * Opening the same ResultDestination multiple times per screen has to be distinguished by manually sending an
 * identifier to the result destination and this identifier has to be propagated back by the result type.
 */
public interface ResultDestination<T : Any> : Destination
