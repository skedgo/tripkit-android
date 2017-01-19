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
    assertThat(resources.getString(R.string.price)).isEqualTo("Price");
    assertThat(resources.getString(R.string.duration)).isEqualTo("Duration");
    assertThat(resources.getString(R.string.preferred)).isEqualTo("Preferred");
    assertThat(resources.getString(R.string.name)).isEqualTo("Name");
    assertThat(resources.getString(R.string.leave_at)).isEqualTo("Leave at");
    assertThat(resources.getString(R.string.arrive_by)).isEqualTo("Arrive by");
    assertThat(resources.getString(R.string.title)).isEqualTo("Title");
    assertThat(resources.getString(R.string.add_when_i_apostm_here)).isEqualTo("Add when I'm here");
    assertThat(resources.getString(R.string.where_do_you_want_to_go_question)).isEqualTo("Where do you want to go?");
    assertThat(resources.getString(R.string.drop_new_pin)).isEqualTo("Drop new pin");
    assertThat(resources.getString(R.string.tap_to_set_location)).isEqualTo("Tap to set location");
  }

  @Test
  @Config(qualifiers = "it")
  public void correctApproximatelyXAwayInItalian() {
    assertThat(resources.getString(R.string.approximately_nps_away))
        .isEqualTo("Circa %s di distanza");
  }

  @Test
  @Config(qualifiers = "pt")
  public void correctFormatOfXMinutesInPortuguese() {
    final String minutes = resources.getString(R.string._perc_dot0f_minutes);
    assertThat(String.format(minutes, 2f))
        .isEqualTo("2 minutos");
  }

  @Test public void correctFormatOfXMinutesInEnglish() {
    final String minutes = resources.getString(R.string._perc_dot0f_minutes);
    assertThat(String.format(minutes, 2f))
        .isEqualTo("2 minutes");
  }

  @Config(qualifiers = "it")
  @Test public void correctFormatOfTripToXInItalian() {
    final String s = resources.getString(R.string.trip_to_nps, "Sydney");
    assertThat(s).isEqualTo("Viaggio a Sydney");
  }

  @Test public void correctFormatOfTripToXInEnglish() {
    final String s = resources.getString(R.string.trip_to_nps, "Sydney");
    assertThat(s).isEqualTo("Trip to Sydney");
  }

  @Test public void shouldHaveCorrectStringForDepartureLocationHint() {
    assertThat(resources.getString(R.string.where_are_you_going_from_question))
        .isEqualTo("Where are you going from?");
  }

  @Test public void shouldHaveCorrectStringForEventName() {
    assertThat(resources.getString(R.string.title))
        .isEqualTo("Title");
  }

  @Test public void shouldHaveCorrectStringForStartDateTime() {
    assertThat(resources.getString(R.string.start))
        .isEqualTo("Start");
  }

  @Test public void shouldHaveCorrectStringForEndDateTime() {
    assertThat(resources.getString(R.string.end_date))
        .isEqualTo("End Date");
  }

  @Test public void shouldHaveCorrectStringForRepeats() {
    assertThat(resources.getString(R.string.repeats))
        .isEqualTo("Repeats");
  }

  @Test public void shouldHaveCorrectStringForAgendaButton() {
    assertThat(resources.getString(R.string.agenda))
        .isEqualTo("Agenda");
  }

  @Test public void shouldHaveCorrectStringForAirlineFlightNumber() {
    assertThat(resources.getString(R.string._pattern_flight__pattern))
        .isEqualTo("%1$s flight %2$s");
  }

  @Test public void shouldHaveCorrectStringForBookTaxi() {
    assertThat(resources.getString(R.string.book_a_taxi))
        .isEqualTo("Book a taxi");
  }

  @Test public void shouldHaveCorrectStringForCalendarEvent() {
    assertThat(resources.getString(R.string.calendar_event))
        .isEqualTo("Event");
  }

  @Test public void shouldHaveCorrectStringForCalendarEventDescription() {
    assertThat(resources.getString(R.string.regular_events_which_will_be_saved_in_your_device_aposts_calendar_dot))
        .isEqualTo("Regular events which will be saved in your device's calendar.");
  }

  @Test public void shouldHaveCorrectStringForCriticalError() {
    assertThat(resources.getString(R.string.critical_error))
        .isEqualTo("Critical Error");
  }

  @Test public void shouldHaveCorrectStringForCriticalErrorFormat() {
    assertThat(resources.getString(R.string.the_application_encountered_a_critical_error_and_can_apostt_recover_2points))
        .isEqualTo("The application encountered a critical error and can't recover:");
  }

  @Test public void shouldHaveCorrectStringForDateFromFormat() {
    assertThat(resources.getString(R.string.from__pattern))
        .isEqualTo("From %1$s");
  }

  @Test public void shouldHaveCorrectStringForFavouritesButton() {
    assertThat(resources.getString(R.string.favourites))
        .isEqualTo("Favourites");
  }

  @Test public void shouldHaveCorrectStringForFlightNumberFormat() {
    assertThat(resources.getString(R.string._pattern_flight__pattern))
        .isEqualTo("%1$s flight %2$s");
  }

  @Test public void shouldHaveCorrectStringForHabitual_Visit() {
    assertThat(resources.getString(R.string.habitual_visit))
        .isEqualTo("Routine");
  }

  @Test public void shouldHaveCorrectStringForHabitualEventDescription() {
    assertThat(resources.getString(R.string.routines_are_part_of_your_daily_movement_coma_but_they_aren_apostt_events_that_you_want_in_your_calendar__start_parente_dotg_dot_coma_work_end_parent_dot_they_are_important_for_the_agenda_to_get_you_to_the_right_location_at_the_right_time_dot))
        .isEqualTo("Routines are part of your daily movement, but they aren't events that you want in your calendar (e.g., work). They are important for the Agenda to get you to the right location at the right time.");
  }

  @Test public void shouldHaveCorrectStringForLocationComingFrom() {
    assertThat(resources.getString(R.string.where_are_you_going_from_question))
        .isEqualTo("Where are you going from?");
  }

  @Test public void shouldHaveCorrectStringForMinutesBefore() {
    assertThat(resources.getString(R.string._perc_dot0f_minutes_before))
        .isEqualTo("%.0f minutes before");
  }

  @Test public void shouldHaveCorrectStringForNewEvent() {
    assertThat(resources.getString(R.string.new_event))
        .isEqualTo("New event");
  }

  @Test public void shouldHaveCorrectStringForNewItemAwayActionTitle() {
    assertThat(resources.getString(R.string.stay_at_a_different_location))
        .isEqualTo("Stay at a different location");
  }

  @Test public void shouldHaveCorrectStringForSearchButton() {
    assertThat(resources.getString(R.string.search))
        .isEqualTo("Search");
  }

  @Test public void shouldHaveCorrectStringForTitleEventName() {
    assertThat(resources.getString(R.string.title))
        .isEqualTo("Title");
  }

  @Test public void shouldHaveCorrectStringForTripsButton() {
    assertThat(resources.getString(R.string.trips))
        .isEqualTo("Trips");
  }

  @Test public void shouldHaveCorrectStringForType() {
    assertThat(resources.getString(R.string.type))
        .isEqualTo("Type");
  }

  @Test public void shouldHaveCorrectStringForWhenIAmAwayHeader() {
    assertThat(resources.getString(R.string.stay))
        .isEqualTo("Stay");
  }

  @Test public void shouldHaveCorrectStringForWhenIAmHereHeader() {
    assertThat(resources.getString(R.string.agenda))
        .isEqualTo("Agenda");
  }

  @Test public void shouldHaveCorrectStringForLongString1() {
    assertThat(resources.getString(R.string.all_your_calendars_are_invisible_coma_please_switch_them_on_in_native_calendar_app_or_account_settings_________))
        .isEqualTo("All your calendars are invisible, please switch them on in native calendar app or account settings");
  }

  @Test public void shouldHaveCorrectStringForLongString2() {
    assertThat(resources.getString(R.string.our_end_user_licence_agreement_has_changed_dot_you_must_agree_to_it_before_continuing_use_of_this_app))
        .isEqualTo("Our End User Licence Agreement has changed. You must agree to it before continuing use of this app");
  }

  @Test public void shouldHaveCorrectStringForLongString3() {
    assertThat(resources.getString(R.string.routines_are_post_of_your_daily_movement_coma_but_they_aren_apostt_events_that_you_want_in_your_calendar__start_parente_dotg_dot_work_end_parent_dot_they_are_important_for_the_agenda_to_get_you_to_the_right_location_at_the_right_time_________))
        .isEqualTo("Routines are post of your daily movement, but they aren't events that you want in your calendar (e.g. work). They are important for the Agenda to get you to the right location at the right time");
  }



}