package com.skedgo.tripkit.booking

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.skedgo.tripkit.common.model.region.Region
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.reactivex.Observable
import okhttp3.HttpUrl
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class AuthServiceImplTest: MockKTest() {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    var api: AuthApi = mockk()
    private lateinit var service: AuthServiceImpl

    @Before
    fun before() {
        MockKAnnotations.init(this)
        initRx()
        service = AuthServiceImpl(api)
    }

    @After
    fun tearDown() {
        tearDownRx()
    }

    @Test
    fun delegateFetchingProviders() {
        val region = mockk<Region>(relaxed = true)

        every { region.getURLs() } returns arrayListOf("https://tripgo.skedgo.com/satapp/")
        every { region.name } returns "The_Ark"
        val url = "https://tripgo.skedgo.com/satapp/auth/The_Ark".toHttpUrlOrNull()

        every { api.fetchProvidersAsync(url!!) } returns Observable.just(emptyList<AuthProvider>())

        service.fetchProvidersByRegionAsync(region, null, false).subscribe()

        verify { api.fetchProvidersAsync(url!!) }
    }

    @Test
    fun fetchingProvidersByModeTest() {
        val region = mockk<Region>(relaxed = true)

        every { region.getURLs() } returns arrayListOf("https://tripgo.skedgo.com/satapp/")
        every { region.name } returns "The_Ark"
        val url = "https://tripgo.skedgo.com/satapp/auth/The_Ark?mode=uber".toHttpUrlOrNull()

        every { api.fetchProvidersAsync(url!!) } returns Observable.just(emptyList<AuthProvider>())

        service.fetchProvidersByRegionAsync(region, "uber", false).subscribe()

        verify { api.fetchProvidersAsync(url!!) }
    }

    @Test
    fun delegateSigningIn() {
        every { api.signInAsync(any()) } returns Observable.just(BookingForm())
        service.signInAsync("Some url")
        verify { api.signInAsync("Some url") }
    }

    @Test
    fun delegateLoggingOut() {
        val mockLogOutResponse = mockk<LogOutResponse>(relaxed = true)
        every { api.logOutAsync(any()) } returns Observable.just(mockLogOutResponse)
        service.logOutAsync("Some url")
        verify { api.logOutAsync("Some url") }
    }
}