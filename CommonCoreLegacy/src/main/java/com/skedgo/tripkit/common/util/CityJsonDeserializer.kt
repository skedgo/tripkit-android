package com.skedgo.tripkit.common.util

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonParseException
import com.skedgo.tripkit.common.model.region.Region.City
import java.lang.reflect.Type

internal class CityJsonDeserializer : JsonDeserializer<City> {
    @Throws(JsonParseException::class)
    override fun deserialize(
        json: JsonElement,
        typeOfT: Type,
        context: JsonDeserializationContext
    ): City {
        val jsonCity = json.asJsonObject
        val title = jsonCity.getAsJsonPrimitive("title").asString
        val lng = jsonCity.getAsJsonPrimitive("lng").asDouble
        val lat = jsonCity.getAsJsonPrimitive("lat").asDouble

        val city = City()
        city.name = title
        city.lat = lat
        city.lon = lng
        return city
    }
}