package com.skedgo.tripkit

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.skedgo.tripkit.Utils.cities
import com.skedgo.tripkit.Utils.findModesByIds
import com.skedgo.tripkit.Utils.matchCityName
import com.skedgo.tripkit.Utils.toModeMap
import com.skedgo.tripkit.common.model.TransportMode
import com.skedgo.tripkit.common.model.TransportMode.Companion.fromId
import com.skedgo.tripkit.common.model.location.Location
import com.skedgo.tripkit.common.model.region.Region
import com.skedgo.tripkit.common.model.region.Region.City
import io.reactivex.Observable
import io.reactivex.observers.TestObserver
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Java6Assertions
import org.junit.Test
import org.junit.runner.RunWith
import java.util.Arrays

@RunWith(AndroidJUnit4::class)
class UtilsTest {

    @Test
    fun `getCities should receive cities from region`() {
        // Arrange
        val region = Region()
        val expectedCities = listOf(Region.City(), Region.City(), Region.City())
        region.cities = ArrayList(expectedCities)

        // Act
        val testObserver: TestObserver<Location> = Observable.just(region)
            .compose(Utils.cities)
            .test()

        // Assert
        testObserver.assertNoErrors()
        val events = testObserver.values()
        assertThat(events).isEqualTo(expectedCities)
    }

    @Test
    fun `getCities should receive empty list for region having empty city list`() {
        // Arrange
        val region = Region()
        region.cities = ArrayList(emptyList())

        // Act
        val testObserver: TestObserver<Location> = Observable.just(region)
            .compose(cities)
            .test()

        // Assert
        testObserver.assertNoErrors()
        testObserver.assertValueSequence(emptyList())
    }

    @Test
    fun `getCities should receive empty list for region having null city list`() {
        // Arrange
        val region = Region()
        region.cities = null

        // Act
        val testObserver: TestObserver<Location> = Observable.just(region)
            .compose(cities)
            .test()

        // Assert
        testObserver.assertNoErrors()
        testObserver.assertValueSequence(emptyList())
    }

    @Test
    fun `matchCityName should return true for null keyword`() {
        val city = Region.City()
        assertThat(Utils.matchCityName(null).test(city)).isTrue
    }

    @Test
    fun `matchCityName should return true for empty keyword`() {
        val city = Region.City()
        assertThat(Utils.matchCityName("").test(city)).isTrue
    }

    @Test
    fun `matchCityName should return true for only space keyword`() {
        val city = Region.City()
        assertThat(Utils.matchCityName("   ").test(city)).isTrue
    }

    @Test
    fun `matchCityName should return true if city name contains keyword`() {
        val city = Region.City().apply { name = "Holy coOl!" }
        assertThat(Utils.matchCityName("Cool").test(city)).isTrue
    }

    @Test
    fun `matchCityName should return false if city name does not contain keyword`() {
        val city = Region.City().apply { name = "Holy cool!" }
        assertThat(Utils.matchCityName("awesome").test(city)).isFalse
    }

    @Test
    fun `findModesByIds should return modes corresponding to requested ids`() {
        // Arrange
        val modeMap = hashMapOf<String, TransportMode>()
        listOf("bus", "car", "motorbike", "taxi", "bicycle").forEach { modeId ->
            val mode = TransportMode().apply { id = modeId }
            modeMap[modeId] = mode
        }

        val modeIds = listOf("car", "motorbike", "bicycle")

        // Act
        val result = Utils.findModesByIds(modeIds).apply(modeMap)

        // Assert
        assertThat(result)
            .isNotNull
            .extracting("id")
            .containsExactlyElementsOf(modeIds)
    }

    @Test
    fun `findModesByIds should ignore modes that are not found`() {
        // Arrange
        val modeMap = hashMapOf<String, TransportMode>()
        listOf("bus", "car", "walk", "taxi", "bicycle").forEach { modeId ->
            val mode = TransportMode().apply { id = modeId }
            modeMap[modeId] = mode
        }

        val modeIds = listOf("car", "motorbike", "bicycle")

        // Act
        val result = Utils.findModesByIds(modeIds).apply(modeMap)

        // Assert
        assertThat(result)
            .isNotNull
            .extracting("id")
            .containsExactly("car", "bicycle")
    }

    @Test
    fun `findModesByIds should return empty list if all modes cannot be found`() {
        // Arrange
        val modeMap = hashMapOf<String, TransportMode>()
        listOf("bus", "tram", "walk", "taxi", "ferry").forEach { modeId ->
            val mode = TransportMode().apply { id = modeId }
            modeMap[modeId] = mode
        }

        val modeIds = listOf("car", "motorbike", "bicycle")

        // Act
        val result = Utils.findModesByIds(modeIds).apply(modeMap)

        // Assert
        assertThat(result).isEmpty()
    }

    @Test
    fun `toModeMap should produce entry having key as mode id`() {
        // Arrange
        val bus = TransportMode.fromId("bus")
        val walk = TransportMode.fromId("walk")
        val car = TransportMode.fromId("car")

        // Act
        val modeMap = Utils.toModeMap().apply(listOf(bus, walk, car))

        // Assert
        assertThat(modeMap)
            .hasSize(3)
            .containsEntry("bus", bus)
            .containsEntry("walk", walk)
            .containsEntry("car", car)
    }
}