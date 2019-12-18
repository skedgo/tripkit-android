package skedgo.tripkit.agenda
import com.google.gson.JsonObject

interface ConfigRepository {

  fun call(): JsonObject
}