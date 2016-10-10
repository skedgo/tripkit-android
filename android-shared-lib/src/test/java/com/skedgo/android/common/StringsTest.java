package com.skedgo.android.common;

import android.content.res.Resources;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(TestRunner.class)
@Config(constants = BuildConfig.class)
public class StringsTest {
  private final Resources resources = RuntimeEnvironment.application.getResources();

  /**
   * To prevent regression issues in
   * https://www.flowdock.com/app/skedgo/androiddev/threads/Q4zIifXMtFIOZPHGanuNCLZobjE.
   */
  @Test public void someEnglishStringsHaveCorrectValues() {
    assertThat(resources.getString(R.string.viewpricebutton)).isEqualTo("Price");
    assertThat(resources.getString(R.string.viewdurationbutton)).isEqualTo("Duration");
    assertThat(resources.getString(R.string.viewoverallbutton)).isEqualTo("Preferred");
    assertThat(resources.getString(R.string.namevehicle)).isEqualTo("Name");
    assertThat(resources.getString(R.string.leaveafter)).isEqualTo("Leave at");
    assertThat(resources.getString(R.string.arrivebefore)).isEqualTo("Arrive by");
    assertThat(resources.getString(R.string.titlefavouritename)).isEqualTo("Title");
    assertThat(resources.getString(R.string.newitemhereactiontitle)).isEqualTo("Add when I'm here");
    assertThat(resources.getString(R.string.locationwanttogo)).isEqualTo("Where do you want to go?");
    assertThat(resources.getString(R.string.dropnewpin)).isEqualTo("Drop new pin");
    assertThat(resources.getString(R.string.setlocation)).isEqualTo("Tap to set location");
  }

  @Test
  @Config(qualifiers = "it")
  public void correctApproximatelyXAwayInItalian() {
    assertThat(resources.getString(R.string.approximately_s_away))
        .isEqualTo("Circa %s di distanza");
  }

  @Test
  @Config(qualifiers = "pt")
  public void correctFormatOfXMinutesInPortuguese() {
    final String minutes = resources.getString(R.string.minutes);
    assertThat(String.format(minutes, 2f))
        .isEqualTo("2 minutos");
  }

  @Test public void correctFormatOfXMinutesInEnglish() {
    final String minutes = resources.getString(R.string.minutes);
    assertThat(String.format(minutes, 2f))
        .isEqualTo("2 minutes");
  }
}