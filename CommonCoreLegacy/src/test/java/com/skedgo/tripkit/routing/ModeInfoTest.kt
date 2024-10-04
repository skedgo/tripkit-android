package com.skedgo.tripkit.routing

import android.text.TextUtils
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.gson.Gson
import com.skedgo.tripkit.common.model.Utils
import org.assertj.core.api.Java6Assertions
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ModeInfoTest {
    @Test
    fun parcel() {
        val expected = createModeInfo()
        val actual = ModeInfo.CREATOR.createFromParcel(Utils.parcel(expected))
        Java6Assertions.assertThat(actual)
            .usingComparator { lhs, rhs ->
                if (TextUtils.equals(lhs.alternativeText, rhs.alternativeText)
                    && TextUtils.equals(lhs.localIconName, rhs.localIconName)
                    && TextUtils.equals(lhs.remoteIconName, rhs.remoteIconName)
                    && TextUtils.equals(lhs.remoteDarkIconName, rhs.remoteDarkIconName)
                    && TextUtils.equals(lhs.description, rhs.description)
                    && TextUtils.equals(lhs.id, rhs.id)
                ) 0
                else -1
            }
            .isEqualTo(expected)
        Java6Assertions.assertThat(actual.color).isEqualTo(expected.color)
    }

    @Test
    fun serializedNames() {
        val info = createModeInfo()
        val json = Gson().toJsonTree(info).asJsonObject
        Java6Assertions.assertThat(json.has("alt")).isTrue()
        Java6Assertions.assertThat(json.has("localIcon")).isTrue()
        Java6Assertions.assertThat(json.has("remoteIcon")).isTrue()
        Java6Assertions.assertThat(json.has("remoteDarkIcon")).isTrue()
        Java6Assertions.assertThat(json.has("description")).isTrue()
        Java6Assertions.assertThat(json.has("identifier")).isTrue()
        Java6Assertions.assertThat(json.has("color")).isTrue()
    }

    private fun createModeInfo(): ModeInfo {
        val info = ModeInfo()
        info.alternativeText = "Taxi"
        info.localIconName = "taxi"
        info.remoteIconName = "taxi"
        info.remoteDarkIconName = "taxi"
        info.description = "Taxi"
        info.id = "ps_tax"
        info.color = ServiceColor(22, 33, 44)
        return info
    }
}