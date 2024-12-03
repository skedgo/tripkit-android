package com.skedgo.geocoding

import com.skedgo.geocoding.GeocodeUtilities.sortByImportance
import com.skedgo.geocoding.agregator.GCResultInterface
import com.skedgo.geocoding.agregator.MGAResultInterface

/**
 * scored result with duplicates
 */
class GroupScoringResult<T : GCResultInterface> : MGAResultInterface<T> {
    override var duplicates: MutableList<MGAResultInterface<T>>? = null
        private set

    override val result: T
        get() = duplicates!![0].result

    override val score: Int
        get() = duplicates!![0].score

    override val classRepresentative: MGAResultInterface<T>
        get() = duplicates!![0]

    override val nameScore: Int
        get() = duplicates!![0].nameScore

    override val addressScore: Int
        get() = duplicates!![0].addressScore

    override val distanceScore: Int
        get() = duplicates!![0].distanceScore

    override val popularityScore: Int
        get() = duplicates!![0].popularityScore


    fun addDuplicate(scoringResult: ScoringResult<T>) {
        if (duplicates == null) duplicates = mutableListOf()
        duplicates?.add(scoringResult)
        duplicates = sortByImportance(duplicates!!).toMutableList()
    }

    fun addDuplicates(scoringResults: List<MGAResultInterface<T>>) {
        if (duplicates == null) duplicates = mutableListOf()
        duplicates?.addAll(scoringResults)
        duplicates = sortByImportance(duplicates!!).toMutableList()
    }

    val scoringResult: ScoringResult<T>
        get() = duplicates!![0] as ScoringResult<T>
}
