package com.skedgo.geocoding

import com.skedgo.geocoding.agregator.GCAppResultInterface
import com.skedgo.geocoding.agregator.GCAppResultInterface.Source.Regions
import com.skedgo.geocoding.agregator.GCFoursquareResultInterface
import com.skedgo.geocoding.agregator.GCGoogleResultInterface
import com.skedgo.geocoding.agregator.GCResultInterface
import com.skedgo.geocoding.agregator.GCSkedGoResultInterface
import com.skedgo.geocoding.agregator.MGAResultInterface


/**
 * scored single result - without duplicates
 */
class ScoringResult<T : GCResultInterface>(
    private val providerResult: T
) : MGAResultInterface<T> {

    override var score: Int = 0
    override var distanceScore: Int = -1
    override var nameScore: Int = -1
    override var addressScore: Int = -1
    override var popularityScore: Int = -1
    override var duplicates: List<MGAResultInterface<T>>? = null
    override var classRepresentative: MGAResultInterface<T>?= null
    override val result: T
        get() = providerResult

    fun equals(element: MGAResultInterface<T>): Boolean {
        return isDuplicate(this, element)
    }

    private fun isDuplicate(mgaResult: MGAResultInterface<T>, mgaResult1: MGAResultInterface<T>): Boolean {
        val result = mgaResult.result
        val result1 = mgaResult1.result

        if (isRegion(result) || isRegion(result1)) return false

        val mgaResultName = mgaResult.result.name
        val mgaResult1Name = mgaResult1.result.name
        val mgaResultLL = LatLng(mgaResult.result.lat ?: 0.0, mgaResult.result.lng ?: 0.0)
        val mgaResult1LL = LatLng(mgaResult1.result.lat ?: 0.0, mgaResult.result.lng ?: 0.0)

        return (mgaResultName.contains(mgaResult1Name) || mgaResult1Name.contains(mgaResultName)) &&
            (mgaResultLL.distanceInMetres(mgaResult1LL) < 10)
    }

    private fun isRegion(result: T): Boolean {
        return result is GCAppResultInterface && result.appResultSource == GCAppResultInterface.Source.Regions
    }

    private fun isBHresult(result: T): Boolean {
        return result is GCAppResultInterface || result is GCSkedGoResultInterface
    }

    private fun isFromDifferentSource(result1: T, result2: T): Boolean {
        return !((result1 is GCAppResultInterface && result2 is GCAppResultInterface) ||
            (result1 is GCSkedGoResultInterface && result2 is GCSkedGoResultInterface) ||
            (result1 is GCGoogleResultInterface && result2 is GCGoogleResultInterface) ||
            (result1 is GCFoursquareResultInterface && result2 is GCFoursquareResultInterface))
    }
}
