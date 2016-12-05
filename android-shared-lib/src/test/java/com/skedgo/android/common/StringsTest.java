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

  @Config(qualifiers = "it")
  @Test public void correctFormatOfTripToXInItalian() {
    final String s = resources.getString(R.string.trip_to, "Sydney");
    assertThat(s).isEqualTo("Viaggio a Sydney");
  }

  @Test public void correctFormatOfTripToXInEnglish() {
    final String s = resources.getString(R.string.trip_to, "Sydney");
    assertThat(s).isEqualTo("Trip to Sydney");
  }

  @Test public void shouldHaveCorrectStringForDepartureLocationHint() {
    assertThat(resources.getString(R.string.locationcomingfrom))
        .isEqualTo("Where are you going from?");
  }

  @Test public void shouldHaveCorrectStringForEventName() {
    assertThat(resources.getString(R.string.titleeventname))
        .isEqualTo("Title");
  }

  @Test public void shouldHaveCorrectStringForStartDateTime() {
    assertThat(resources.getString(R.string.eventstartdatetime))
        .isEqualTo("Start");
  }

  @Test public void shouldHaveCorrectStringForEndDateTime() {
    assertThat(resources.getString(R.string.eventenddatetime))
        .isEqualTo("End");
  }

  @Test public void shouldHaveCorrectStringForRepeats() {
    assertThat(resources.getString(R.string.eventrepeat))
        .isEqualTo("Repeats");
  }

  @Test public void shouldHaveCorrectStringForAgendaButton() {
    assertThat(resources.getString(R.string.agendabutton))
        .isEqualTo("Agenda");
  }

  @Test public void shouldHaveCorrectStringForAirlineFlightNumber() {
    assertThat(resources.getString(R.string.airlineflightnumber))
        .isEqualTo("%1$s flight %2$s");
  }

  @Test public void shouldHaveCorrectStringForAlertTitleFormat() {
    assertThat(resources.getString(R.string.alerttitleformat))
        .isEqualTo("Alert: %1$s");
  }

  @Test public void shouldHaveCorrectStringForBookTaxi() {
    assertThat(resources.getString(R.string.booktaxi))
        .isEqualTo("Book a taxi");
  }

  @Test public void shouldHaveCorrectStringForCalendarEvent() {
    assertThat(resources.getString(R.string.calendar_event))
        .isEqualTo("Event");
  }

  @Test public void shouldHaveCorrectStringForCalendarEventDescription() {
    assertThat(resources.getString(R.string.calendareventdescription))
        .isEqualTo("Regular events which will be saved in your device's calendar.");
  }

  @Test public void shouldHaveCorrectStringForCriticalError() {
    assertThat(resources.getString(R.string.criticalerror))
        .isEqualTo("Critical Error");
  }

  @Test public void shouldHaveCorrectStringForCriticalErrorFormat() {
    assertThat(resources.getString(R.string.criticalerrorformat))
        .isEqualTo("The application encountered a critical error and can't recover:");
  }

  @Test public void shouldHaveCorrectStringForDateFromFormat() {
    assertThat(resources.getString(R.string.datefromformat))
        .isEqualTo("From %1$s");
  }

  @Test public void shouldHaveCorrectStringForEditItemHereTitle() {
    assertThat(resources.getString(R.string.edititemheretitle))
        .isEqualTo("Agenda Item");
  }

  @Test public void shouldHaveCorrectStringForExcuseDueToLate() {
    assertThat(resources.getString(R.string.excuseduetolate))
        .isEqualTo("TripGo - There goes my excuse for being late...");
  }

  @Test public void shouldHaveCorrectStringForFavouritesButton() {
    assertThat(resources.getString(R.string.favouritesbutton))
        .isEqualTo("Favourites");
  }

  @Test public void shouldHaveCorrectStringForFlightNumberFormat() {
    assertThat(resources.getString(R.string.flightnumberformat))
        .isEqualTo("Flight %1$s");
  }

  @Test public void shouldHaveCorrectStringForFreeDirections() {
    assertThat(resources.getString(R.string.freedirections))
        .isEqualTo("TripGo - Free directions");
  }

  @Test public void shouldHaveCorrectStringForFromToForEventLocationFormat() {
    assertThat(resources.getString(R.string.fromtoforeventlocationformat))
        .isEqualTo("From %1$s to %2$s");
  }

  @Test public void shouldHaveCorrectStringForGetAround() {
    assertThat(resources.getString(R.string.getaround))
        .isEqualTo("I use TripGo to get around.");
  }

  @Test public void shouldHaveCorrectStringForHabitual_Visit() {
    assertThat(resources.getString(R.string.habitual_visit))
        .isEqualTo("Routine");
  }

  @Test public void shouldHaveCorrectStringForHabitualEventDescription() {
    assertThat(resources.getString(R.string.habitualeventdescription))
        .isEqualTo("Routines are part of your daily movement, but they aren't events that you want in your calendar (e.g., work). They are important for the Agenda to get you to the right location at the right time.");
  }

  @Test public void shouldHaveCorrectStringForLocationComingFrom() {
    assertThat(resources.getString(R.string.locationcomingfrom))
        .isEqualTo("Where are you going from?");
  }

  @Test public void shouldHaveCorrectStringForLostOnce() {
    assertThat(resources.getString(R.string.lostonce))
        .isEqualTo("I once was lost but now I have found... TripGo.");
  }

  @Test public void shouldHaveCorrectStringForMinutesBefore() {
    assertThat(resources.getString(R.string.minutesbefore))
        .isEqualTo("%.0f minutes before");
  }

  @Test public void shouldHaveCorrectStringForNewEvent() {
    assertThat(resources.getString(R.string.newevent))
        .isEqualTo("New Event");
  }

  @Test public void shouldHaveCorrectStringForNewItemAwayActionTitle() {
    assertThat(resources.getString(R.string.newitemawayactiontitle))
        .isEqualTo("Stay at a different location");
  }

  @Test public void shouldHaveCorrectStringForNewStayTitle() {
    assertThat(resources.getString(R.string.newstaytitle))
        .isEqualTo("New Stay");
  }

  @Test public void shouldHaveCorrectStringForNumberRouteFormat() {
    assertThat(resources.getString(R.string.numberrouteformat))
        .isEqualTo("Route %1$s of %2$s");
  }

  @Test public void shouldHaveCorrectStringForSearchButton() {
    assertThat(resources.getString(R.string.searchbutton))
        .isEqualTo("Search");
  }

  @Test public void shouldHaveCorrectStringForTitleEventName() {
    assertThat(resources.getString(R.string.titleeventname))
        .isEqualTo("Title");
  }

  @Test public void shouldHaveCorrectStringForTransportGenius() {
    assertThat(resources.getString(R.string.transportgenius))
        .isEqualTo("TripGo - Transport genius");
  }

  @Test public void shouldHaveCorrectStringForTransportModeWish() {
    assertThat(resources.getString(R.string.transportmodewish))
        .isEqualTo("TripGo - How do you want to go today?");
  }

  @Test public void shouldHaveCorrectStringForTripsButton() {
    assertThat(resources.getString(R.string.tripsbutton))
        .isEqualTo("Trips");
  }

  @Test public void shouldHaveCorrectStringForTypeEvent() {
    assertThat(resources.getString(R.string.typeevent))
        .isEqualTo("Type");
  }

  @Test public void shouldHaveCorrectStringForTypeVehicle() {
    assertThat(resources.getString(R.string.typevehicle))
        .isEqualTo("Type");
  }

  @Test public void shouldHaveCorrectStringForWhenIAmAwayHeader() {
    assertThat(resources.getString(R.string.wheniamawayheader))
        .isEqualTo("Stays");
  }

  @Test public void shouldHaveCorrectStringForWhenIAmHereHeader() {
    assertThat(resources.getString(R.string.wheniamhereheader))
        .isEqualTo("Agenda");
  }


}