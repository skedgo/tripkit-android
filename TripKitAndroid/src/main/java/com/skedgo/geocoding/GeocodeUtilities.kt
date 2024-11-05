package com.skedgo.geocoding

import com.skedgo.geocoding.agregator.GCAppResultInterface
import com.skedgo.geocoding.agregator.GCFoursquareResultInterface
import com.skedgo.geocoding.agregator.GCResultInterface
import com.skedgo.geocoding.agregator.GCSkedGoResultInterface
import com.skedgo.geocoding.agregator.MGAResultInterface
import java.util.Collections
import java.util.Locale
import kotlin.math.abs
import kotlin.math.ceil
import kotlin.math.sqrt

object GeocodeUtilities {
    fun scoreBasedOnNameMatchBetweenSearchTerm(searchTerm: String, candidate: String): Int {
        var searchTerm = searchTerm
        var candidate = candidate
        searchTerm = stringForScoringOfString(searchTerm)
        candidate = stringForScoringOfString(candidate)

        return scoreBetweenSearchTerm(searchTerm, candidate)
    }

    fun stringForScoringOfString(term: String?): String {
        var updatedTerm = ""
        if (term != null) updatedTerm = term.trim { it <= ' ' }

        updatedTerm = updatedTerm.replace("\\s+".toRegex(), " ")
        var result = ""
        for (character in updatedTerm.toCharArray()) {
            if (isAlphanumeric(character)) result += character.toString()
                .lowercase(Locale.getDefault())
        }
        return result
    }

    fun scoreBetweenSearchTerm(target: String, candidate: String): Int {
        if (target.isEmpty()) return if (candidate.isEmpty()) 100 else 0

        if (candidate.isEmpty()) return 100 // having typed yet means a perfect match of everything you've typed so far


        if (target.equals(candidate, ignoreCase = true)) {
            return 100
        }

        if (isAbbreviationFor(target, candidate)) return 95

        if (isAbbreviationFor(candidate, target)) {
            return 90
        }

        val excess = candidate.length - target.length
        val fullMatchRangeLocation = candidate.indexOf(target)
        if (fullMatchRangeLocation == 0) {
            // matches right at start
            return calculateScoring(100, excess, 75)
        } else if (fullMatchRangeLocation != -1) {
            val before = candidate.substring(fullMatchRangeLocation - 1, fullMatchRangeLocation)
            return if (before.matches("\\S".toRegex())) {
                // matches beginning of word
                calculateScoring(75, fullMatchRangeLocation * 2 + excess, 33)
            } else {
                // in-word match
                calculateScoring(40, excess, 15)
            }
        }

        //         non-substring matches
        val targetWords = target.split(" ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        var lastIndex = 0
        for (targetWord in targetWords) {
            val location = candidate.indexOf(targetWord)
            if (location == -1) {
                return 0 // missing a word!
            } else if (location >= lastIndex) {
                // still in order, keep going
                lastIndex = location
            } else {
                // wrong order, abort with penalty
                return calculateScoring(10, excess, 0)
            }
        }

        // contains all target words in order
        // do we have all the finished words
        for (i in 0 until targetWords.size - 1) {
            val targetWord = targetWords[i]
            val after = candidate.indexOf(targetWord) + targetWord.length + 1
            if (candidate[after].toString().matches("\\S".toRegex())) {
                // full word match, continue with next
            } else {
                //  candidate doesn't have a completed word
                return calculateScoring(33, excess, 10)
            }
        }

        return calculateScoring(66, excess, 40)
    }

    //    It resolves abbreviations such as ("MOMA", "museum of modern art")
    fun isAbbreviationFor(abbreviation: String, text: String): Boolean {
        var abbreviation = abbreviation
        var text = text
        abbreviation = abbreviation.lowercase(Locale.getDefault())
        text = text.lowercase(Locale.getDefault())
        if (abbreviation.length <= 2) {
            return false
        }

        var letter = abbreviation.substring(0, 1)
        if (!text.startsWith(letter)) return false

        val parts = text.split(" ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        if (parts.size != abbreviation.length) return false

        for (i in 1 until abbreviation.length) {
            letter = abbreviation.substring(i, i + 1)
            val word = parts[i]
            if (!word.startsWith(letter)) return false
        }

        return true
    }

    private fun calculateScoring(maximum: Int, penalty: Int, minimum: Int): Int {
        return if (penalty > maximum - minimum) {
            minimum
        } else {
            maximum - penalty
        }
    }

    fun rangedScoreForScore(score: Int, minimum: Int, maximum: Int): Int {
        var score = score
        if (score > 100) score = 100
        val range = maximum - minimum
        val percentage = score / 100f
        return ceil((percentage * range).toDouble()).toInt() + minimum
    }

    fun scoreBasedOnDistanceFromCoordinate(
        coordinate: LatLng,
        region: GCBoundingBox?,
        regionCenter: LatLng,
        longDistance: Boolean
    ): Int {
        //        That's covering the special case of passing in the whole world. In that case everything scores 100%.

        val worldRegion = GCBoundingBox.World
        if (region != null && (abs(worldRegion.latitudeDelta - region.latitudeDelta) < 1 &&
                abs(worldRegion.longitudeDelta - region.longitudeDelta) < 1)
        ) return 100

        val meters = coordinate.distanceInMetres(regionCenter)
        val zeroScoreDistance = (if (longDistance) 20000000 else 25000).toDouble()
        if (meters >= zeroScoreDistance) {
            return 0
        }

        val match =
            if (longDistance) sqrt(meters) / sqrt(zeroScoreDistance) else meters / zeroScoreDistance
        val proportion = 1.0 - match
        val max = 100
        val score = (proportion * max).toInt()
        return score
    }


    private fun isAlphanumeric(character: Char): Boolean {
        return (Character.isLetter(character) || Character.isDigit(character) || character.toString()
            .matches("\\S".toRegex()))
    }

    fun isSuburb(foursquareResult: GCFoursquareResultInterface): Boolean {
        var isSuburb = false

        for (categoryName in foursquareResult.categories) {
            if (categoryName.equals("States & Municipalities", ignoreCase = true)) {
                isSuburb = true
                break
            }
        }
        return isSuburb
    }

    fun <T : GCResultInterface> sortByImportance(scoreResults: List<MGAResultInterface<T>>): List<MGAResultInterface<T>> {
        Collections.sort(scoreResults) { o1, o2 ->
            compareInt(
                getRanking2Group(o2),
                getRanking2Group(o1)
            )
        }

        return scoreResults
    }

    fun <T : GCResultInterface> sortByScore(scoreResults: List<MGAResultInterface<T>>): List<MGAResultInterface<T>> {
        Collections.sort(scoreResults) { o1, o2 -> compareInt(o2.score, o1.score) }
        return scoreResults
    }

    fun compareInt(x: Int, y: Int): Int {
        return if ((x < y)) -1 else (if ((x == y)) 0 else 1)
    }

    private fun getRanking2Group(element: MGAResultInterface<*>): Int {
        if (element.result is GCSkedGoResultInterface) return 10

        if (element.result is GCAppResultInterface) {
            val appResult = element.result as GCAppResultInterface
            return if (appResult.isFavourite) 11
            else 9
        }

        if (element.result is GCFoursquareResultInterface) {
            val foursquareResult = element.result as GCFoursquareResultInterface
            return if (foursquareResult.isVerified) 8
            else 6
        }

        return 7
    }
}
