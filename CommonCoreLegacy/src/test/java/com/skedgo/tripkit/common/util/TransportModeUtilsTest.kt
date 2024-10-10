package com.skedgo.tripkit.common.util

import android.annotation.TargetApi
import android.content.res.Resources
import android.os.Build.VERSION_CODES
import android.util.DisplayMetrics
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.skedgo.tripkit.common.model.TransportMode
import com.skedgo.tripkit.routing.ModeInfo
import io.mockk.every
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Java6Assertions
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito

@RunWith(AndroidJUnit4::class)
class TransportModeUtilsTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private lateinit var resources: Resources

    @Before
    fun before() {
        resources = mockk()
    }

    @Test
    fun iconUrlForId_nonEmptyId() {
        every { resources.displayMetrics } returns createDisplayMetricsByDpi(DisplayMetrics.DENSITY_XHIGH)
        val iconId = "pt-opal"
        assertThat(TransportModeUtils.getIconUrlForId(resources, iconId))
            .isEqualTo("https://static.skedgo.com/icons/android/xhdpi/ic_transport_pt-opal.png")
    }

    @Test
    fun iconUrlForId_nullId() {
        every { resources.displayMetrics } returns createDisplayMetricsByDpi(DisplayMetrics.DENSITY_XHIGH)
        assertThat(TransportModeUtils.getIconUrlForId(resources, null)).isNull()
    }

    @Test
    fun iconUrlForId_emptyId() {
        every { resources.displayMetrics } returns createDisplayMetricsByDpi(DisplayMetrics.DENSITY_XHIGH)
        assertThat(TransportModeUtils.getIconUrlForId(resources, "")).isNull()
    }

    @Test
    fun iconUrlForModeInfo_nonNull() {
        every { resources.displayMetrics } returns createDisplayMetricsByDpi(DisplayMetrics.DENSITY_XHIGH)
        val modeInfo = ModeInfo().apply {
            remoteIconName = "pt-opal"
        }
        assertThat(TransportModeUtils.getIconUrlForModeInfo(resources, modeInfo))
            .isEqualTo("https://static.skedgo.com/icons/android/xhdpi/ic_transport_pt-opal.png")
    }

    @Test
    fun iconUrlForModeInfo_null() {
        every { resources.displayMetrics } returns createDisplayMetricsByDpi(DisplayMetrics.DENSITY_XHIGH)
        assertThat(TransportModeUtils.getIconUrlForModeInfo(resources, null)).isNull()
    }

    @Test
    fun iconUrlForModeInfo_nonNullModeInfoButNullIconName() {
        every { resources.displayMetrics } returns createDisplayMetricsByDpi(DisplayMetrics.DENSITY_XHIGH)
        val modeInfo = ModeInfo().apply {
            remoteIconName = ""
        }
        assertThat(TransportModeUtils.getIconUrlForModeInfo(resources, modeInfo)).isNull()
    }

    @Test
    fun darkIconUrlForModeInfo_nonNull() {
        every { resources.displayMetrics } returns createDisplayMetricsByDpi(DisplayMetrics.DENSITY_XHIGH)
        val modeInfo = ModeInfo().apply {
            remoteDarkIconName = "lyft-dark"
        }
        assertThat(TransportModeUtils.getDarkIconUrlForModeInfo(resources, modeInfo))
            .isEqualTo("https://static.skedgo.com/icons/android/xhdpi/ic_transport_lyft-dark.png")
    }

    @Test
    fun darkIconUrlForModeInfo_null() {
        every { resources.displayMetrics } returns createDisplayMetricsByDpi(DisplayMetrics.DENSITY_XHIGH)
        assertThat(TransportModeUtils.getDarkIconUrlForModeInfo(resources, null)).isNull()
    }

    @Test
    fun darkIconUrlForModeInfo_nonNullModeInfoButNullIconName() {
        every { resources.displayMetrics } returns createDisplayMetricsByDpi(DisplayMetrics.DENSITY_XHIGH)
        val modeInfo = ModeInfo().apply {
            remoteDarkIconName = null
        }
        assertThat(TransportModeUtils.getDarkIconUrlForModeInfo(resources, modeInfo)).isNull()
    }

    @Test
    fun iconUrlForTransportMode_nonNull() {
        every { resources.displayMetrics } returns createDisplayMetricsByDpi(DisplayMetrics.DENSITY_XHIGH)
        val mode = TransportMode().apply {
            iconId = "pt-opal"
        }
        assertThat(TransportModeUtils.getIconUrlForTransportMode(resources, mode))
            .isEqualTo("https://static.skedgo.com/icons/android/xhdpi/ic_transport_pt-opal.png")
    }

    @Test
    fun iconUrlForTransportMode_nonNullTransportModeButNullIconId() {
        every { resources.displayMetrics } returns createDisplayMetricsByDpi(DisplayMetrics.DENSITY_XHIGH)
        val mode = TransportMode().apply {
            iconId = null
        }
        assertThat(TransportModeUtils.getIconUrlForTransportMode(resources, mode)).isNull()
    }

    @Test
    fun iconUrlForTransportMode_null() {
        every { resources.displayMetrics } returns createDisplayMetricsByDpi(DisplayMetrics.DENSITY_XHIGH)
        assertThat(TransportModeUtils.getIconUrlForTransportMode(resources, null)).isNull()
    }

    @Test
    fun darkIconUrlForTransportMode_nonNull() {
        every { resources.displayMetrics } returns createDisplayMetricsByDpi(DisplayMetrics.DENSITY_XHIGH)
        val mode = TransportMode().apply {
            darkIcon = "some-dark-icon"
        }
        assertThat(TransportModeUtils.getDarkIconUrlForTransportMode(resources, mode))
            .isEqualTo("https://static.skedgo.com/icons/android/xhdpi/ic_transport_some-dark-icon.png")
    }

    @Test
    fun darkIconUrlForTransportMode_nonNullTransportModeButNullIconId() {
        every { resources.displayMetrics } returns createDisplayMetricsByDpi(DisplayMetrics.DENSITY_XHIGH)
        val mode = TransportMode().apply {
            darkIcon = null
        }
        assertThat(TransportModeUtils.getDarkIconUrlForTransportMode(resources, mode)).isNull()
    }

    @Test
    fun darkIconUrlForTransportMode_null() {
        every { resources.displayMetrics } returns createDisplayMetricsByDpi(DisplayMetrics.DENSITY_XHIGH)
        assertThat(TransportModeUtils.getDarkIconUrlForTransportMode(resources, null)).isNull()
    }

    @Test
    fun shouldReturnDensityDpiNameCorrectly() {
        assertThat(TransportModeUtils.getDensityDpiName(DisplayMetrics.DENSITY_MEDIUM)).isEqualTo("mdpi")
        assertThat(TransportModeUtils.getDensityDpiName(DisplayMetrics.DENSITY_HIGH)).isEqualTo("hdpi")
        assertThat(TransportModeUtils.getDensityDpiName(DisplayMetrics.DENSITY_XHIGH)).isEqualTo("xhdpi")
        assertThat(TransportModeUtils.getDensityDpiName(DisplayMetrics.DENSITY_XXHIGH)).isEqualTo("xxhdpi")
        assertThat(TransportModeUtils.getDensityDpiName(DisplayMetrics.DENSITY_XXXHIGH)).isEqualTo("xxhdpi")
    }

    private fun createDisplayMetricsByDpi(dpi: Int): DisplayMetrics {
        return DisplayMetrics().apply { densityDpi = dpi }
    }
}