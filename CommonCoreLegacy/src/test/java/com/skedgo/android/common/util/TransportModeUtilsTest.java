package com.skedgo.android.common.util;

import android.annotation.TargetApi;
import android.content.res.Resources;
import android.os.Build;
import android.support.annotation.NonNull;
import android.util.DisplayMetrics;

import com.skedgo.android.common.BuildConfig;
import com.skedgo.android.common.TestRunner;
import com.skedgo.android.common.model.ModeInfo;
import com.skedgo.android.common.model.TransportMode;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.annotation.Config;

import static android.util.DisplayMetrics.DENSITY_HIGH;
import static android.util.DisplayMetrics.DENSITY_MEDIUM;
import static android.util.DisplayMetrics.DENSITY_XHIGH;
import static android.util.DisplayMetrics.DENSITY_XXHIGH;
import static android.util.DisplayMetrics.DENSITY_XXXHIGH;
import static com.skedgo.android.common.util.TransportModeUtils.getDarkIconUrlForModeInfo;
import static com.skedgo.android.common.util.TransportModeUtils.getDarkIconUrlForTransportMode;
import static com.skedgo.android.common.util.TransportModeUtils.getDensityDpiName;
import static com.skedgo.android.common.util.TransportModeUtils.getIconUrlForId;
import static com.skedgo.android.common.util.TransportModeUtils.getIconUrlForModeInfo;
import static com.skedgo.android.common.util.TransportModeUtils.getIconUrlForTransportMode;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(TestRunner.class)
@Config(constants = BuildConfig.class)
public class TransportModeUtilsTest {
  private Resources resources;

  @Before public void before() {
    resources = mock(Resources.class);
  }

  @Test public void getIconUrlForId_nonEmptyId() {
    when(resources.getDisplayMetrics()).thenReturn(createDisplayMetricsByDpi(DENSITY_XHIGH));
    final String iconId = "pt-opal";
    assertThat(getIconUrlForId(resources, iconId))
        .isEqualTo("https://tripgo.skedgo.com/satapp/modeicons/android/xhdpi/ic_transport_pt-opal.png");
  }

  @Test public void getIconUrlForId_nullId() {
    when(resources.getDisplayMetrics()).thenReturn(createDisplayMetricsByDpi(DENSITY_XHIGH));
    assertThat(getIconUrlForId(resources, null)).isNull();
  }

  @Test public void getIconUrlForId_emptyId() {
    when(resources.getDisplayMetrics()).thenReturn(createDisplayMetricsByDpi(DENSITY_XHIGH));
    assertThat(getIconUrlForId(resources, "")).isNull();
  }

  @Test public void getIconUrlForModeInfo_nonNull() {
    when(resources.getDisplayMetrics()).thenReturn(createDisplayMetricsByDpi(DENSITY_XHIGH));
    final ModeInfo modeInfo = new ModeInfo();
    modeInfo.setRemoteIconName("pt-opal");
    assertThat(getIconUrlForModeInfo(resources, modeInfo))
        .isEqualTo("https://tripgo.skedgo.com/satapp/modeicons/android/xhdpi/ic_transport_pt-opal.png");
  }

  @Test public void getIconUrlForModeInfo_null() {
    when(resources.getDisplayMetrics()).thenReturn(createDisplayMetricsByDpi(DENSITY_XHIGH));
    assertThat(getIconUrlForModeInfo(resources, null)).isNull();
  }

  @Test public void getIconUrlForModeInfo_nonNullModeInfoButNullIconName() {
    when(resources.getDisplayMetrics()).thenReturn(createDisplayMetricsByDpi(DENSITY_XHIGH));
    final ModeInfo modeInfo = new ModeInfo();
    modeInfo.setRemoteIconName(null);
    assertThat(getIconUrlForModeInfo(resources, modeInfo)).isNull();
  }

  @Test public void getDarkIconUrlForModeInfo_nonNull() {
    when(resources.getDisplayMetrics()).thenReturn(createDisplayMetricsByDpi(DENSITY_XHIGH));
    final ModeInfo modeInfo = new ModeInfo();
    modeInfo.setRemoteDarkIconName("lyft-dark");
    assertThat(getDarkIconUrlForModeInfo(resources, modeInfo))
        .isEqualTo("https://tripgo.skedgo.com/satapp/modeicons/android/xhdpi/ic_transport_lyft-dark.png");
  }

  @Test public void getDarkIconUrlForModeInfo_null() {
    when(resources.getDisplayMetrics()).thenReturn(createDisplayMetricsByDpi(DENSITY_XHIGH));
    assertThat(getDarkIconUrlForModeInfo(resources, null)).isNull();
  }

  @Test public void getDarkIconUrlForModeInfo_nonNullModeInfoButNullIconName() {
    when(resources.getDisplayMetrics()).thenReturn(createDisplayMetricsByDpi(DENSITY_XHIGH));
    final ModeInfo modeInfo = new ModeInfo();
    modeInfo.setRemoteDarkIconName(null);
    assertThat(getDarkIconUrlForModeInfo(resources, modeInfo)).isNull();
  }

  @Test public void getIconUrlForTransportMode_nonNull() {
    when(resources.getDisplayMetrics()).thenReturn(createDisplayMetricsByDpi(DENSITY_XHIGH));
    final TransportMode mode = new TransportMode();
    mode.setIconId("pt-opal");
    assertThat(getIconUrlForTransportMode(resources, mode))
        .isEqualTo("https://tripgo.skedgo.com/satapp/modeicons/android/xhdpi/ic_transport_pt-opal.png");
  }

  @Test public void getIconUrlForTransportMode_nonNullTransportModeButNullIconId() {
    when(resources.getDisplayMetrics()).thenReturn(createDisplayMetricsByDpi(DENSITY_XHIGH));
    final TransportMode mode = new TransportMode();
    mode.setIconId(null);
    assertThat(getIconUrlForTransportMode(resources, mode)).isNull();
  }

  @Test public void getIconUrlForTransportMode_null() {
    when(resources.getDisplayMetrics()).thenReturn(createDisplayMetricsByDpi(DENSITY_XHIGH));
    assertThat(getIconUrlForTransportMode(resources, null)).isNull();
  }

  @Test public void getDarkIconUrlForTransportMode_nonNull() {
    when(resources.getDisplayMetrics()).thenReturn(createDisplayMetricsByDpi(DENSITY_XHIGH));
    final TransportMode mode = new TransportMode();
    mode.setDarkIcon("some-dark-icon");
    assertThat(getDarkIconUrlForTransportMode(resources, mode))
        .isEqualTo("https://tripgo.skedgo.com/satapp/modeicons/android/xhdpi/ic_transport_some-dark-icon.png");
  }

  @Test public void getDarkIconUrlForTransportMode_nonNullTransportModeButNullIconId() {
    when(resources.getDisplayMetrics()).thenReturn(createDisplayMetricsByDpi(DENSITY_XHIGH));
    final TransportMode mode = new TransportMode();
    mode.setIconId(null);
    assertThat(getDarkIconUrlForTransportMode(resources, mode)).isNull();
  }

  @Test public void getDarkIconUrlForTransportMode_null() {
    when(resources.getDisplayMetrics()).thenReturn(createDisplayMetricsByDpi(DENSITY_XHIGH));
    assertThat(getDarkIconUrlForTransportMode(resources, null)).isNull();
  }

  @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2) @Test
  public void shouldReturnDensityDpiNameCorrectly() {
    assertThat(getDensityDpiName(DENSITY_MEDIUM)).isEqualTo("mdpi");
    assertThat(getDensityDpiName(DENSITY_HIGH)).isEqualTo("hdpi");
    assertThat(getDensityDpiName(DENSITY_XHIGH)).isEqualTo("xhdpi");
    assertThat(getDensityDpiName(DENSITY_XXHIGH)).isEqualTo("xxhdpi");
    assertThat(getDensityDpiName(DENSITY_XXXHIGH)).isEqualTo("xxhdpi");
  }

  @NonNull private DisplayMetrics createDisplayMetricsByDpi(int dpi) {
    final DisplayMetrics displayMetrics = new DisplayMetrics();
    displayMetrics.densityDpi = dpi;
    return displayMetrics;
  }
}