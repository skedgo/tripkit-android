package com.skedgo.geocoding.agregator

import java.io.Serializable

/**
 * this class represents a scored result, it could be single or have duplicates
 */
interface MGAResultInterface<T : GCResultInterface> : Serializable {

    // Returns query result
    val result: T

    // Total score for the result
    val score: Int

    // Returns all duplicates for the result
    val duplicates: List<MGAResultInterface<T>>?

    // Returns the scored result which is the class representative for the duplicate list
    val classRepresentative: MGAResultInterface<T>?

    // Name score value
    val nameScore: Int

    // Address score value
    val addressScore: Int

    // Distance score value
    val distanceScore: Int

    // Popularity score value
    val popularityScore: Int
}
