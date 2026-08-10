package com.skedgo.tripkit.configuration

import org.assertj.core.api.Java6Assertions.assertThat
import org.junit.Test

class ServerTest {
    @Test
    fun `default server base URLs end with a slash`() {
        DefaultServer.values().forEach {
            assertThat(it.value).endsWith("/")
        }
    }
}
