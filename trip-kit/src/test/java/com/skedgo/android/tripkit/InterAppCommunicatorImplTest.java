package com.skedgo.android.tripkit;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import rx.functions.Action1;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
public class InterAppCommunicatorImplTest {
  @Mock PackageManager packageManager;

  @Before public void before() {
    MockitoAnnotations.initMocks(this);
  }

  @Test
  public void testReturnIntentToLaunchUberAppOrSite() throws PackageManager.NameNotFoundException {
    when(packageManager.getApplicationInfo("com.ubercab", PackageManager.GET_ACTIVITIES))
        .thenReturn(new ApplicationInfo());

    new InterAppCommunicatorImpl(packageManager).
        performExternalAction("uber", new Action1<String>() {
          @Override public void call(String link) {
            assertThat(link).isNotNull();
            assertThat(link).isEqualTo("uber://");
          }
        }, new Action1<String>() {
          @Override public void call(String s) {
            assertThat(false);
          }
        });

  }

  @Test
  public void testReturnIntentToLaunchFlytwaysAppOrSite() throws PackageManager.NameNotFoundException {
    new InterAppCommunicatorImpl(packageManager).
        performExternalAction("flitways", new Action1<String>() {
          @Override public void call(String s) {
            assertThat(false);
          }
        }, new Action1<String>() {
          @Override public void call(String link) {

            // this link should be handled by the client
            assertThat(link).isNull();
          }
        });
  }

  @Test
  public void testReturnIntentToLaunchLyftIntentSite() throws PackageManager.NameNotFoundException {
    when(packageManager.getApplicationInfo("me.lyft.android", PackageManager.GET_ACTIVITIES))
        .thenReturn(new ApplicationInfo());

    new InterAppCommunicatorImpl(packageManager).
        performExternalAction("lyft", new Action1<String>() {
          @Override public void call(String link) {
            assertThat(link).isNotNull();
            assertThat(link).isEqualTo("lyft://");
          }
        }, new Action1<String>() {
          @Override public void call(String s) {
            assertThat(false);
          }
        });
  }

  @Test
  public void testReturnIntentToLaunchAnySite() throws PackageManager.NameNotFoundException {
    final String googleUrl = "http://www.google.com";
    new InterAppCommunicatorImpl(packageManager).
        performExternalAction(googleUrl, new Action1<String>() {
          @Override public void call(String s) {
            assertThat(false);
          }
        }, new Action1<String>() {
          @Override public void call(String link) {

            // this link should be handled by the client
            assertThat(link).isEqualTo(googleUrl);
          }
        });
  }
}