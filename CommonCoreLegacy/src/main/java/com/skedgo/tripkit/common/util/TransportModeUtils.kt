package com.skedgo.tripkit.common.util

import android.content.res.Resources
import android.util.DisplayMetrics
import com.skedgo.tripkit.common.model.TransportMode
import com.skedgo.tripkit.configuration.ServerManager.configuration
import com.skedgo.tripkit.routing.ModeInfo

object TransportModeUtils {
    val ICON_URL_TEMPLATE: String =
        configuration.staticTripGoUrl + "icons/android/%s/ic_transport_%s.png"

    @JvmStatic
    fun getIconUrlForId(
        resources: Resources,
        iconId: String?
    ): String? {
        if (iconId == null || iconId.length == 0) {
            return null
        }

        val densityDpiName = getDensityDpiName(resources.displayMetrics.densityDpi)
        return String.format(ICON_URL_TEMPLATE, densityDpiName, iconId)
    }

    @JvmStatic
    fun getIconUrlForModeInfo(
        resources: Resources,
        modeInfo: ModeInfo?
    ): String? {
        if (modeInfo == null) {
            return null
        }

        return getIconUrlForId(resources, modeInfo.remoteIconName)
    }

    fun getDarkIconUrlForModeInfo(
        resources: Resources,
        modeInfo: ModeInfo?
    ): String? {
        if (modeInfo == null) {
            return null
        }

        return getIconUrlForId(resources, modeInfo.remoteDarkIconName)
    }

    fun getIconUrlForTransportMode(
        resources: Resources,
        mode: TransportMode?
    ): String? {
        if (mode == null) {
            return null
        }

        return getIconUrlForId(resources, mode.iconId)
    }

    fun getDarkIconUrlForTransportMode(
        resources: Resources,
        mode: TransportMode?
    ): String? {
        if (mode == null) {
            return null
        }

        return getIconUrlForId(resources, mode.darkIcon)
    }

    fun getDensityDpiName(densityDpi: Int): String {
        return when (densityDpi) {
            DisplayMetrics.DENSITY_MEDIUM -> "mdpi"
            DisplayMetrics.DENSITY_HIGH -> "hdpi"
            DisplayMetrics.DENSITY_XHIGH -> "xhdpi"
            DisplayMetrics.DENSITY_XXHIGH -> "xxhdpi"
            else -> "xxhdpi"
        }
    }
}