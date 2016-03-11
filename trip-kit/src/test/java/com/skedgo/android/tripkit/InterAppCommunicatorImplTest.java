package com.skedgo.android.tripkit;

import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.Uri;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
public class InterAppCommunicatorImplTest {

  @Test
  public void testReturnIntentToLaunchUberAppOrSite() throws PackageManager.NameNotFoundException {

    final PackageManager packageManager = mock(PackageManager.class);
    when(packageManager.getApplicationInfo("com.ubercab", PackageManager.GET_ACTIVITIES))
        .thenReturn(new ApplicationInfo());

    final Intent intent = new InterAppCommunicatorImpl().getUberIntent(packageManager);
    assertThat(intent).isNotNull();
    assertThat(intent.getAction()).isEqualTo(Intent.ACTION_VIEW);
    assertThat(intent.getData()).isEqualTo(Uri.parse("uber://"));
  }

  @Test
  public void testReturnIntentToLaunchSupportedBookingApi() {
    String link = ("http://BARYOGENESIS.SKEDGO.COM/satapp/booking/1f6bc155-7e8a-4edf-bb0c-7925d1ed5d40/info");

    final Intent intent = new InterAppCommunicatorImpl().getSupportBookingIntent(link);
    assertThat(intent).isNotNull();
    assertThat(intent.getAction()).isEqualTo(InterAppCommunicator.ACTION_BOOK);
    assertThat(intent.getStringExtra(InterAppCommunicator.KEY_URL))
        .isEqualTo(link);
  }

  @Test
  public void testReturnIntentToLaunchFlitWaysAppOrSite() throws PackageManager.NameNotFoundException {

    String flitWaysPartnerKey = "key";
    String pick = "pick";
    String destination = "destination";
    String date = "date";

    final Intent intent = new InterAppCommunicatorImpl().getFlitWaysIntent(flitWaysPartnerKey, pick, destination, date);
    assertThat(intent).isNotNull();
    assertThat(intent.getAction()).isEqualTo(Intent.ACTION_VIEW);
    assertThat(intent.getData()).isEqualTo(Uri.parse("https://flitways.com/api/link?partner_key=" + flitWaysPartnerKey + "&pick=" + pick + "&destination=" + destination + "&trip_date=" + date));
  }

  @Test
  public void testReturnIntentToLaunchLyftIntentSite() throws PackageManager.NameNotFoundException {

    final PackageManager packageManager = mock(PackageManager.class);
    when(packageManager.getApplicationInfo("me.lyft.android", PackageManager.GET_ACTIVITIES))
        .thenReturn(new ApplicationInfo());

    final Intent intent = new InterAppCommunicatorImpl().getLyftIntent("publisher id", packageManager);
    assertThat(intent).isNotNull();
    assertThat(intent.getAction()).isEqualTo(Intent.ACTION_VIEW);
    assertThat(intent.getData()).isEqualTo(Uri.parse("lyft://"));
  }

  @Test
  public void testReturnIntentToLaunchAnySite() throws PackageManager.NameNotFoundException {

    String link = "www.google.com";

    final Intent intent = new InterAppCommunicatorImpl().getURLIntent(link);
    assertThat(intent).isNotNull();
    assertThat(intent.getAction()).isEqualTo(Intent.ACTION_VIEW);
    assertThat(intent.getData()).isEqualTo(Uri.parse(link));
  }

}