package com.skedgo.geocoding.agregator

import com.skedgo.geocoding.GCBoundingBox
import com.skedgo.geocoding.GCQuery
import com.skedgo.geocoding.GeocodeUtilities
import com.skedgo.geocoding.LatLng
import com.skedgo.geocoding.ScoringResult
import kotlin.math.max
import kotlin.math.min

/**
 * Scoring formulas
 * Favourites: max(title, address)
 * Search history: max(title, address)
 * Regions: distance
 * Address book: (title + address) / 2
 * Calendar: (title + address) / 2
 * SkedGo transit stops: popularity   -> ((min(popularity, GOOD_SCORE)) / (GOOD_SCORE / 100)) * 2
 * SkedGo others: title
 * Google: (max(title, address) * 3 + distance) / 4
 * Google Autocomplete: (title * 3) / 4
 * Foursquare: ((title * 3 + distance) / 4) * suburb
 * Title score: score based on input string against the title of the result
 * Address score: score based on input string against address of the result
 * Distance score: score based on distance to center or provided region
 * Suburb score: bonus score if result is a suburb
 * Popularity score: score based on popularity of result as determined by server
 */
class MultiSourceGeocodingAggregator<T : GCResultInterface> private constructor() {

    companion object {
        private var instance: MultiSourceGeocodingAggregator<*>? = null

        @Suppress("UNCHECKED_CAST")
        fun <T : GCResultInterface> getInstance(): MultiSourceGeocodingAggregator<T> {
            if (instance == null) {
                instance = MultiSourceGeocodingAggregator<T>()
            }
            return instance as MultiSourceGeocodingAggregator<T>
        }
    }

    fun aggregate(userQuery: GCQueryInterface, providersResults: List<List<T>>): List<MGAResultInterface<T>> {
        val query: GCQuery = if (userQuery is GCQuery) {
            userQuery
        } else {
            if (userQuery.bounds is GCBoundingBox) {
                GCQuery(userQuery.queryText, userQuery.bounds as GCBoundingBox)
            } else {
                GCQuery(userQuery.queryText, GCBoundingBox(userQuery.bounds))
            }
        }

        val scoredResults = mutableListOf<MGAResultInterface<T>>()
        providersResults.forEach { providerResults ->
            providerResults.forEach { candidate ->
                calculateScore(query, candidate)?.let { scoringResult ->
                    if (scoringResult.score != 0) {
                        scoredResults.add(scoringResult)
                    }
                }
            }
        }

        return scoredResults.sortedByDescending { it.score }
    }

    fun flattenAggregate(userQuery: GCQueryInterface, providersResults: List<List<T>>): List<GCResultInterface> {
        val aggregates = aggregate(userQuery, providersResults)
        return aggregates.map { it.result }
    }

    private fun calculateScore(query: GCQuery, candidate: T): ScoringResult<T>? {
        return when (candidate) {
            is GCGoogleResultInterface -> selectGoogleScore(query, candidate)
            is GCFoursquareResultInterface -> calculateFoursquareScoring(query, candidate)
            is GCSkedGoResultInterface -> calculateSkedGoScoring(query, candidate)
            is GCAppResultInterface -> when (candidate.appResultSource) {
                GCAppResultInterface.Source.History -> calculateHistoryScoring(query, candidate)
                GCAppResultInterface.Source.Calendar -> calculateCalendarScoring(query, candidate)
                GCAppResultInterface.Source.AddressBook -> calculateAddressBookScoring(query, candidate)
                GCAppResultInterface.Source.Regions -> calculateRegionsScoring(query, candidate)
            }
            else -> null
        }
    }

    private fun selectGoogleScore(query: GCQuery, candidate: GCGoogleResultInterface): ScoringResult<T> {
        return if (candidate.lat != null && candidate.lng != null && candidate.address != null) {
            calculateGoogleScoring(query, candidate)
        } else {
            calculateAutocompleteScore(query, candidate)
        }
    }

    @Suppress("UNCHECKED_CAST")
    private fun calculateGoogleScoring(query: GCQuery, candidate: GCGoogleResultInterface): ScoringResult<T> {
        val scoringResult = ScoringResult<T>(candidate as T)
        val nameScore = GeocodeUtilities.scoreBasedOnNameMatchBetweenSearchTerm(query.queryText, candidate.name)
        val addressScore = GeocodeUtilities.scoreBasedOnNameMatchBetweenSearchTerm(query.queryText, candidate.address)
        val stringScore = maxOf(nameScore, addressScore)

        scoringResult.nameScore = nameScore
        scoringResult.addressScore = addressScore

        val distanceScore = if (candidate.lat != -1.0 && candidate.lng != -1.0) {
            val coordinate = LatLng(candidate.lat ?: 0.0, candidate.lng ?: 0.0)
            GeocodeUtilities.scoreBasedOnDistanceFromCoordinate(coordinate, query.bounds, query.bounds.center(), false).also {
                scoringResult.distanceScore = it
            }
        } else 0

        val rawScore = (stringScore * 3 + distanceScore) / 4
        val totalScore = GeocodeUtilities.rangedScoreForScore(rawScore, 15, 75)
        scoringResult.score = totalScore
        return scoringResult
    }
    @Suppress("UNCHECKED_CAST")
    private fun calculateAutocompleteScore(query: GCQuery, candidate: GCGoogleResultInterface): ScoringResult<T> {
        val scoringResult = ScoringResult<T>(candidate as T)
        val nameScore = GeocodeUtilities.scoreBasedOnNameMatchBetweenSearchTerm(query.queryText, candidate.name)
        val rawScore = (nameScore * 3) / 4
        val totalScore = GeocodeUtilities.rangedScoreForScore(rawScore, 15, 75)
        scoringResult.score = totalScore
        return scoringResult
    }
    @Suppress("UNCHECKED_CAST")
    private fun calculateFoursquareScoring(query: GCQuery, candidate: GCFoursquareResultInterface): ScoringResult<T>? {
        if (!candidate.isVerified && (candidate.categories == null || candidate.categories.isEmpty())) return null

        val scoringResult = ScoringResult<T>(candidate as T)
        val titleScore = GeocodeUtilities.scoreBasedOnNameMatchBetweenSearchTerm(query.queryText, candidate.name)
        if (titleScore == 0) {
            scoringResult.score = 0
            scoringResult.nameScore = 0
            return scoringResult
        }

        val coordinate = LatLng(candidate.lat ?: 0.0, candidate.lng ?: 0.0)
        val distanceScore = GeocodeUtilities.scoreBasedOnDistanceFromCoordinate(coordinate, query.bounds, query.bounds.center(), false)
        val rawScore = (titleScore * 3 + distanceScore) / 4 * if (GeocodeUtilities.isSuburb(candidate)) 2 else 1

        scoringResult.nameScore = titleScore
        scoringResult.distanceScore = distanceScore

        val (min, max) = if (candidate.isVerified) 33 to 66 else 15 to 66
        val totalScore = GeocodeUtilities.rangedScoreForScore(rawScore, min, max)
        scoringResult.score = totalScore
        return scoringResult
    }

    @Suppress("UNCHECKED_CAST")
    private fun calculateSkedGoScoring(query: GCQuery, candidate: GCSkedGoResultInterface): ScoringResult<T> {
        val scoringResult = ScoringResult<T>(candidate as T)
        val score = if (candidate.resultClass.equals("StopLocation", ignoreCase = true)) {
            val GOOD_SCORE = 1000
            val popularityScore = ((minOf(candidate.popularity, GOOD_SCORE) / (GOOD_SCORE / 100)) * 2).let {
                if (candidate.modeIdentifiers?.isNotEmpty() == true)
                    GeocodeUtilities.rangedScoreForScore(it, 50, 90)
                else GeocodeUtilities.rangedScoreForScore(it, 30, 80)
            }
            if (candidate.popularity > GOOD_SCORE) {
                popularityScore + GeocodeUtilities.rangedScoreForScore(popularityScore / GOOD_SCORE, 0, 10)
            } else popularityScore
        } else {
            if (query.queryText.isNotEmpty()) {
                val nameScore = GeocodeUtilities.scoreBasedOnNameMatchBetweenSearchTerm(query.queryText, candidate.name)
                if (candidate.modeIdentifiers?.isNotEmpty() == true) {
                    GeocodeUtilities.rangedScoreForScore(nameScore, 50, 90)
                } else GeocodeUtilities.rangedScoreForScore(nameScore, 0, 50)
            } else GeocodeUtilities.rangedScoreForScore(candidate.popularity, 0, 50)
        }
        scoringResult.popularityScore = candidate.popularity
        scoringResult.score = score
        return scoringResult
    }

    @Suppress("UNCHECKED_CAST")
    private fun calculateHistoryScoring(query: GCQuery, candidate: GCAppResultInterface): ScoringResult<T> {
        val scoringResult = ScoringResult<T>(candidate as T)
        val rawScore = if (query.queryText.isNotEmpty()) {
            val nameScore = GeocodeUtilities.scoreBasedOnNameMatchBetweenSearchTerm(query.queryText, candidate.name)
            val addressScore = GeocodeUtilities.scoreBasedOnNameMatchBetweenSearchTerm(query.queryText, candidate.subtitle)
            scoringResult.nameScore = nameScore
            scoringResult.addressScore = addressScore
            maxOf(nameScore, addressScore)
        } else {
            scoringResult.nameScore = 100
            100
        }
        val (min, max) = if (candidate.isFavourite) 90 to 100 else 50 to 90
        scoringResult.score = GeocodeUtilities.rangedScoreForScore(rawScore, min, max)
        return scoringResult
    }

    @Suppress("UNCHECKED_CAST")
    private fun calculateCalendarScoring(query: GCQueryInterface, candidate: GCAppResultInterface): ScoringResult<T> {
        val scoringResult = ScoringResult<T>(candidate as T)
        val nameScore = GeocodeUtilities.scoreBasedOnNameMatchBetweenSearchTerm(query.queryText, candidate.name)
        val locationScore = GeocodeUtilities.scoreBasedOnNameMatchBetweenSearchTerm(query.queryText, candidate.subtitle)
        scoringResult.nameScore = nameScore
        scoringResult.addressScore = locationScore

        val rawScore = minOf(100, (nameScore + locationScore) / 2)
        scoringResult.score = GeocodeUtilities.rangedScoreForScore(rawScore, 50, 90)
        return scoringResult
    }

    private fun calculateAddressBookScoring(query: GCQueryInterface, candidate: GCAppResultInterface) = calculateCalendarScoring(query, candidate)

    @Suppress("UNCHECKED_CAST")
    private fun calculateRegionsScoring(query: GCQuery, candidate: GCAppResultInterface): ScoringResult<T> {
        val scoringResult = ScoringResult<T>(candidate as T)
        val coordinate = LatLng(candidate.lat ?: 0.0, candidate.lng ?: 0.0)
        val rawScore = GeocodeUtilities.scoreBasedOnDistanceFromCoordinate(coordinate, query.bounds, query.bounds.center(), false)
        scoringResult.distanceScore = rawScore

        val totalScore = GeocodeUtilities.rangedScoreForScore(rawScore, 50, 90)
        scoringResult.score = totalScore
        return scoringResult
    }
}
