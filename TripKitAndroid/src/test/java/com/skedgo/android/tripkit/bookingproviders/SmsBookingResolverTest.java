package com.skedgo.android.tripkit.bookingproviders;

import android.content.Intent;
import android.net.Uri;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import static org.assertj.core.api.Java6Assertions.assertThat;

@RunWith(RobolectricTestRunner.class)
public class SmsBookingResolverTest {
  @Test public void createSmsIntentByActionHavingBothNumberAndBody() {
    final Intent intent = SmsBookingResolver.createSmsIntentByAction("sms:12345?Hello Android!");
    assertThat(intent).isNotNull();
    assertThat(intent.getData()).isEqualTo(Uri.parse("sms:12345"));
    assertThat(intent.getStringExtra("sms_body")).isEqualTo("Hello Android!");
  }

  @Test public void createSmsIntentByActionHavingOnlyNumber() {
    final Intent intent = SmsBookingResolver.createSmsIntentByAction("sms:12345");
    assertThat(intent).isNotNull();
    assertThat(intent.getData()).isEqualTo(Uri.parse("sms:12345"));
    assertThat(intent.getStringExtra("sms_body")).isNull();
  }

  @Test public void createSmsIntentByActionHavingOnlyNumberAndQuestionMark() {
    final Intent intent = SmsBookingResolver.createSmsIntentByAction("sms:12345?");
    assertThat(intent).isNotNull();
    assertThat(intent.getData()).isEqualTo(Uri.parse("sms:12345"));
    assertThat(intent.getStringExtra("sms_body")).isNull();
  }
}