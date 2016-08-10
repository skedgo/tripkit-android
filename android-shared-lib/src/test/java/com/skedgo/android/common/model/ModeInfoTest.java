package com.skedgo.android.common.model;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.skedgo.android.common.BuildConfig;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import java.util.Comparator;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
public class ModeInfoTest {
  @Test public void parcel() {
    final ModeInfo expected = createModeInfo();
    final ModeInfo actual = ModeInfo.CREATOR.createFromParcel(Utils.parcel(expected));
    Assertions.assertThat(actual)
        .usingComparator(new Comparator<ModeInfo>() {
          @Override
          public int compare(ModeInfo lhs, ModeInfo rhs) {
            return TextUtils.equals(lhs.getAlternativeText(), rhs.getAlternativeText())
                && TextUtils.equals(lhs.getLocalIconName(), rhs.getLocalIconName())
                && TextUtils.equals(lhs.getRemoteIconName(), rhs.getRemoteIconName())
                && TextUtils.equals(lhs.getRemoteDarkIconName(), rhs.getRemoteDarkIconName())
                && TextUtils.equals(lhs.getDescription(), rhs.getDescription())
                && TextUtils.equals(lhs.getId(), rhs.getId())
                ? 0
                : -1;
          }
        })
        .isEqualTo(expected);
    assertThat(actual.getColor()).isEqualTo(expected.getColor());
  }

  @Test public void serializedNames() {
    final ModeInfo info = createModeInfo();
    final JsonObject json = new Gson().toJsonTree(info).getAsJsonObject();
    assertThat(json.has("alt")).isTrue();
    assertThat(json.has("localIcon")).isTrue();
    assertThat(json.has("remoteIcon")).isTrue();
    assertThat(json.has("remoteDarkIcon")).isTrue();
    assertThat(json.has("description")).isTrue();
    assertThat(json.has("identifier")).isTrue();
    assertThat(json.has("color")).isTrue();
  }

  private ModeInfo createModeInfo() {
    final ModeInfo info = new ModeInfo();
    info.setAlternativeText("Taxi");
    info.setLocalIconName("taxi");
    info.setRemoteIconName("taxi");
    info.setRemoteDarkIconName("taxi");
    info.setDescription("Taxi");
    info.setId("ps_tax");
    info.setColor(new ServiceColor(22, 33, 44));
    return info;
  }
}