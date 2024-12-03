package com.skedgo.tripkit

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.skedgo.tripkit.a2brouting.FailoverA2bRoutingApi
import com.skedgo.tripkit.booking.ui.base.MockKTest
import com.skedgo.tripkit.common.model.Query
import com.skedgo.tripkit.common.model.location.Location
import com.skedgo.tripkit.common.model.time.TimeTag
import com.skedgo.tripkit.data.tsp.RegionInfo
import com.skedgo.tripkit.routing.ExtraQueryMapProvider
import com.skedgo.tripkit.tsp.RegionInfoRepository
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.mockk
import io.reactivex.Observable
import org.assertj.core.api.Assertions.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class RouteServiceImplTest: MockKTest() {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private val queryGenerator: QueryGenerator = mockk()
    private val co2Preferences: Co2Preferences = mockk()
    private val tripPreferences: TripPreferences = mockk()
    private val extraQueryMapProvider: ExtraQueryMapProvider = mockk()
    private val routingApi: FailoverA2bRoutingApi = mockk()
    private val regionInfoRepository: RegionInfoRepository = mockk()
    private val regionInfo: RegionInfo = mockk()
    private lateinit var routeService: RouteServiceImpl

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        initRx()
        routeService = RouteServiceImpl(
            ApplicationProvider.getApplicationContext<Context>(),
            queryGenerator,
            co2Preferences,
            tripPreferences,
            extraQueryMapProvider,
            routingApi,
            regionInfoRepository
        )

        every { extraQueryMapProvider.call() } returns emptyMap()
        every { regionInfoRepository.getRegionInfoByRegion(any()) } returns Observable.just(regionInfo)
        every { regionInfo.transitWheelchairAccessibility() } returns false

        every { extraQueryMapProvider.call() } returns emptyMap()
        every { regionInfoRepository.getRegionInfoByRegion(any()) } returns Observable.just(regionInfo)
        every { regionInfo.transitWheelchairAccessibility() } returns false

        // Mock the call to isConcessionPricingPreferred
        every { tripPreferences.isConcessionPricingPreferred() } returns false
        every { co2Preferences.getCo2Profile() } returns mapOf("a" to 2f, "b" to 5f)
    }

    @After
    fun tearDown() {
        tearDownRx()
    }

    @Test
    fun `should include some options`() {
        val query = createQuery().apply {
            timeTag = TimeTag.createForArriveBy(25251325)
            cyclingSpeed = 3
        }

        val options = routeService.toOptions(query)

        assertThat(options)
            .containsEntry("v", "12")
            .containsEntry("unit", query.unit)
            .containsEntry("from", "(1.0,2.0)")
            .containsEntry("to", "(3.0,4.0)")
            .containsEntry("arriveBefore", "25251325")
            .containsEntry("departAfter", "0")
            .containsEntry("tt", "2")
            .containsEntry("ws", "4")
            .containsEntry("cs", "3")
            .doesNotContainKey("ir")
    }

    @Test
    fun `should include address string`() {
        val query = createQuery().apply {
            fromLocation?.address = "from address"
            toLocation?.address = "to address"
        }

        val options = routeService.toOptions(query)

        assertThat(options)
            .containsEntry("from", "(1.0,2.0)\"from address\"")
            .containsEntry("to", "(3.0,4.0)\"to address\"")
    }

    @Test
    fun `should include extra query map`() {
        val query = createQuery().apply {
            timeTag = TimeTag.createForArriveBy(25251325)
        }

        val extraQueryMap = mapOf("bsb" to 1)
        every { extraQueryMapProvider.call() } returns extraQueryMap

        val options = routeService.toOptions(query)

        assertThat(options).containsEntry("bsb", 1)
    }

    @Test
    fun `include concession pricing`() {
        every { tripPreferences.isConcessionPricingPreferred() } returns true
        assertThat(routeService.getParamsByPreferences()).containsEntry("conc", true)
    }

    @Test
    fun `exclude concession pricing`() {
        every { tripPreferences.isConcessionPricingPreferred() } returns false
        assertThat(routeService.getParamsByPreferences()).doesNotContainKey("conc")
    }

    @Test
    fun `exclude wheelchair info`() {
        every { tripPreferences.isWheelchairPreferred() } returns false
        assertThat(routeService.getParamsByPreferences()).doesNotContainKey("wheelchair")
    }

    @Test
    fun `should include option depart after`() {
        val query = createQuery().apply {
            timeTag = TimeTag.createForLeaveAfter(25251325)
        }

        val options = routeService.toOptions(query)

        assertThat(options)
            .containsEntry("arriveBefore", "0")
            .containsEntry("departAfter", "25251325")
    }

    @Test
    fun `should contain option include stops`() {
        val query = createQuery()

        val options = routeService.toOptions(query)

        assertThat(options).containsEntry("includeStops", "1")
    }

    @Test
    fun `include CO2 profile`() {
        val co2Profile = mapOf("a" to 2f, "b" to 5f)
        every { co2Preferences.getCo2Profile() } returns co2Profile

        assertThat(routeService.getParamsByPreferences())
            .hasSize(2)
            .containsEntry("co2[a]", 2f)
            .containsEntry("co2[b]", 5f)
    }

    private fun createQuery(): Query {
        return Query().apply {
            fromLocation = Location(1.0, 2.0)
            toLocation = Location(3.0, 4.0)
            transferTime = 2
            walkingSpeed = 4
            unit = "mi"
        }
    }
}
