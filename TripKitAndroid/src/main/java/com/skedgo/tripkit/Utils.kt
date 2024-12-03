package com.skedgo.tripkit

import android.text.TextUtils
import com.skedgo.tripkit.common.model.TransportMode
import com.skedgo.tripkit.common.model.location.Location
import com.skedgo.tripkit.common.model.region.Region
import com.skedgo.tripkit.common.model.region.Region.City
import io.reactivex.Observable
import io.reactivex.ObservableTransformer
import io.reactivex.functions.Function
import io.reactivex.functions.Predicate
import org.apache.commons.collections4.CollectionUtils
import java.util.Locale

object Utils {
    @JvmStatic
    val cities: ObservableTransformer<Region, Location>
        get() = object : ObservableTransformer<Region, Location> {
            override fun apply(observable: Observable<Region>): Observable<Location> {
                return observable
                    .flatMap(Function { region: Region ->
                        val cities = region.cities
                        if (cities != null) {
                            return@Function Observable.fromIterable<City>(cities)
                        } else {
                            return@Function Observable.empty<City>()
                        }
                    })
                    .map { city: City -> city }
            }
        }

    /**
     * @return True for either null or "" string or string having only spaces.
     * Also true if the city name contains the keyword. Otherwise, false.
     */
    @JvmStatic
    fun matchCityName(name: String?): Predicate<Location> {
        val lowerCaseName = name?.lowercase(Locale.getDefault())
        return Predicate { city: Location ->
            val name1 = city.name
            isNullOrEmpty(lowerCaseName) ||
                (name1 != null && name1.lowercase(Locale.getDefault()).contains(
                    lowerCaseName.orEmpty()
                ))
        }
    }

    fun isNullOrEmpty(s: CharSequence?): Boolean {
        return s == null || TextUtils.getTrimmedLength(s) == 0
    }

    fun findModesByIds(modeIds: List<String>): Function<Map<String, TransportMode>, List<TransportMode>> {
        return Function { modeMap: Map<String, TransportMode?> ->
            val modes: MutableList<TransportMode> = ArrayList(modeIds.size)
            for (modeId in modeIds) {
                val mode = modeMap[modeId]
                if (mode != null) {
                    modes.add(mode)
                }
            }
            modes
        }
    }

    @JvmStatic
    fun toModeMap(): Function<List<TransportMode>, Map<String, TransportMode>> {
        return Function<List<TransportMode>, Map<String, TransportMode>> { modes ->
            val modeMap = HashMap<String, TransportMode>()
            for (mode in modes) {
                modeMap[mode.id] = mode
            }
            modeMap
        }
    }

    fun <T> isNotEmpty(): Predicate<List<T>> {
        return Predicate<List<T>> { items -> CollectionUtils.isNotEmpty(items) }
    }
}