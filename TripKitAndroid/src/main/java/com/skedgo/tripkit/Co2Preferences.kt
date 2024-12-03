package com.skedgo.tripkit

interface Co2Preferences {
    /**
     * @return An immutable map having key as mode identifier for
     * which to apply emissions, and its value is emissions for
     * the supplied mode identifier in grams of CO2 per kilometre.
     */
    fun getCo2Profile(): Map<String, Float>

    /**
     * @param modeId        Mode identifier for which to apply these emissions.
     * @param gramsCO2PerKm Emissions for supplied mode identifier in grams of CO2 per kilometre.
     */
    fun setEmissions(modeId: String, gramsCO2PerKm: Float)
}