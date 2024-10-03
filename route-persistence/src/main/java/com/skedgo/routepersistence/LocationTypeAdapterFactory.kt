package com.skedgo.routepersistence

import com.google.gson.Gson
import com.google.gson.TypeAdapter
import com.google.gson.TypeAdapterFactory
import com.google.gson.reflect.TypeToken
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonToken.NULL
import com.google.gson.stream.JsonWriter
import com.skedgo.tripkit.common.model.location.Location
import java.io.IOException

/**
 * Reads and writes only bare essentials of [Location]
 * when persisting [TripSegment] as JSON.
 */
class LocationTypeAdapterFactory : TypeAdapterFactory {
    override fun <T> create(gson: Gson, type: TypeToken<T>): TypeAdapter<T>? {
        if (LocationTypeAdapter.adapts(type)) {
            return LocationTypeAdapter(gson) as TypeAdapter<T>
        }
        return null
    }

    private class LocationTypeAdapter(gson: Gson?) : TypeAdapter<Location?>() {
        @Throws(IOException::class)
        override fun write(out: JsonWriter, value: Location?) {
            if (value == null) {
                out.nullValue()
            } else {
                writeLocation(out, value)
            }
        }

        @Throws(IOException::class)
        override fun read(`in`: JsonReader): Location? {
            if (`in`.peek() == NULL) {
                `in`.nextNull()
                return null
            }
            return readLocation(`in`)
        }

        @Throws(IOException::class)
        private fun writeLocation(out: JsonWriter, instance: Location) {
            out.beginObject()
            val nameValue = instance.name
            if (nameValue != null) {
                out.name("name")
                out.value(nameValue)
            } else if (out.serializeNulls) {
                out.name("name")
                out.nullValue()
            }
            val addressValue = instance.address
            if (addressValue != null) {
                out.name("address")
                out.value(addressValue)
            } else if (out.serializeNulls) {
                out.name("address")
                out.nullValue()
            }
            val timezoneValue = instance.timeZone
            if (timezoneValue != null) {
                out.name("timezone")
                out.value(timezoneValue)
            } else if (out.serializeNulls) {
                out.name("timezone")
                out.nullValue()
            }
            val regionValue = instance.region
            if (regionValue != null) {
                out.name("region")
                out.value(regionValue)
            } else if (out.serializeNulls) {
                out.name("region")
                out.nullValue()
            }
            out.name("lat")
            out.value(instance.lat)
            out.name("lng")
            out.value(instance.lon)
            out.name("bearing")
            out.value(instance.bearing.toLong())
            out.endObject()
        }

        @Throws(IOException::class)
        private fun readLocation(`in`: JsonReader): Location {
            val location = Location()
            `in`.beginObject()
            while (`in`.hasNext()) {
                eachAttribute(`in`, location)
            }
            `in`.endObject()
            return location
        }

        @Throws(IOException::class)
        private fun eachAttribute(`in`: JsonReader, location: Location) {
            val attributeName = `in`.nextName()
            when (attributeName[0]) {
                'n' -> if ("name" == attributeName) {
                    readInName(`in`, location)
                    return
                }
                'a' -> if ("address" == attributeName) {
                    readInAddress(`in`, location)
                    return
                }
                't' -> if ("timezone" == attributeName) {
                    readInTimezone(`in`, location)
                    return
                }
                'l' -> {
                    if ("lat" == attributeName) {
                        readInLat(`in`, location)
                        return
                    }
                    if ("lng" == attributeName) {
                        readInLng(`in`, location)
                        return
                    }
                }
                'b' -> if ("bearing" == attributeName) {
                    readInBearing(`in`, location)
                    return
                }
                'r' -> if ("region" == attributeName) {
                    readInRegion(`in`, location)
                    return
                }
                else -> {}
            }
            `in`.skipValue()
        }

        @Throws(IOException::class)
        private fun readInName(`in`: JsonReader, instance: Location) {
            if (`in`.peek() == NULL) {
                `in`.nextNull()
            } else {
                instance.name = `in`.nextString()
            }
        }

        @Throws(IOException::class)
        private fun readInAddress(`in`: JsonReader, instance: Location) {
            if (`in`.peek() == NULL) {
                `in`.nextNull()
            } else {
                instance.address = `in`.nextString()
            }
        }

        @Throws(IOException::class)
        private fun readInTimezone(`in`: JsonReader, instance: Location) {
            if (`in`.peek() == NULL) {
                `in`.nextNull()
            } else {
                instance.timeZone = `in`.nextString()
            }
        }

        @Throws(IOException::class)
        private fun readInLat(`in`: JsonReader, instance: Location) {
            instance.lat = `in`.nextDouble()
        }

        @Throws(IOException::class)
        private fun readInLng(`in`: JsonReader, instance: Location) {
            instance.lon = `in`.nextDouble()
        }

        @Throws(IOException::class)
        private fun readInBearing(`in`: JsonReader, instance: Location) {
            instance.bearing = `in`.nextInt()
        }

        @Throws(IOException::class)
        private fun readInRegion(`in`: JsonReader, instance: Location) {
            instance.region = `in`.nextString()
        }

        companion object {
            fun adapts(type: TypeToken<*>): Boolean {
                return Location::class.java == type.rawType
            }
        }
    }
}
