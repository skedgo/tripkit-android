package com.skedgo.routepersistence;

import android.util.Pair;

import skedgo.tripkit.routing.TripGroup;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 22)
public class WhereClausesTest {
  @Test public void shouldCreateClauseToMatchUuid() {
    final TripGroup group = mock(TripGroup.class);
    when(group.uuid()).thenReturn("Some id");

    final Pair<String, String[]> r = WhereClauses.matchesUuidOf(group);

    assertThat(r.first).isEqualTo("uuid = ?");
    assertThat(r.second).isEqualTo(new String[] {"Some id"});
  }

  @Test public void shouldCreateClauseToDetectPastRoutes() {
    final Pair<String, String[]> r = WhereClauses.happenedBefore(2, TimeUnit.DAYS.toMillis(5L));
    assertThat(r.first).isEqualTo(
        "EXISTS (" +
            "SELECT * FROM trips " +
            "WHERE tripGroups.uuid = trips.groupId " +
            "AND tripGroups.displayTripId = trips.id " +
            "AND trips.arrive < ?" +
            ")"
    );
    assertThat(r.second).isEqualTo(new String[] {"424800"});
  }
}
