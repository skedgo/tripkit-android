//package com.skedgo.tripkit.configuration
//
//import org.assertj.core.api.Java6Assertions.assertThat
//import org.junit.Test
//
//class ServerTest {
//    @Test
//    fun `Server should not end with a slash`() {
//        Server.values().forEach {
//            if (it != Server.BigBang)
//                assertThat(it.value).endsWith("/")
//            else
//                assertThat(it.value).doesNotEndWith("/")
//        }
//    }
//}