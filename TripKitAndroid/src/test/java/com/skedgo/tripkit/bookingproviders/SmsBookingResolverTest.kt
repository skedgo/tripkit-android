package com.skedgo.tripkit.bookingproviders

import android.net.Uri
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SmsBookingResolverTest {

    @Test
    fun `create SMS intent by action having both number and body`() {
        // Act
        val intent = SmsBookingResolver.createSmsIntentByAction("sms:12345?Hello Android!")

        // Assert
        assertThat(intent).isNotNull
        assertThat(intent.data).isEqualTo(Uri.parse("sms:12345"))
        assertThat(intent.getStringExtra("sms_body")).isEqualTo("Hello Android!")
    }

    @Test
    fun `create SMS intent by action having only number`() {
        // Act
        val intent = SmsBookingResolver.createSmsIntentByAction("sms:12345")

        // Assert
        assertThat(intent).isNotNull
        assertThat(intent.data).isEqualTo(Uri.parse("sms:12345"))
        assertThat(intent.getStringExtra("sms_body")).isNull()
    }

    @Test
    fun `create SMS intent by action having only number and question mark`() {
        // Act
        val intent = SmsBookingResolver.createSmsIntentByAction("sms:12345?")

        // Assert
        assertThat(intent).isNotNull
        assertThat(intent.data).isEqualTo(Uri.parse("sms:12345"))
        assertThat(intent.getStringExtra("sms_body")).isNull()
    }
}