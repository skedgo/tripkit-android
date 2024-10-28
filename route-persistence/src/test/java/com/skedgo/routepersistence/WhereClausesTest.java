package com.skedgo.routepersistence;

import android.util.Pair;

import com.skedgo.tripkit.routing.TripGroup;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.TimeUnit;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(AndroidJUnit4.class)
public class WhereClausesTest {

    // TODO: Unit test - refactor
    /* Disabled function due to Mockito Exception
    @Test
    public void shouldCreateClauseToMatchUuid() {
        final TripGroup group = mock(TripGroup.class);
        when(group.uuid()).thenReturn("Some id");

        final Pair<String, String[]> r = WhereClauses.matchesUuidOf(group);

        assertThat(r.first).isEqualTo("uuid = ?");
        assertThat(r.second).isEqualTo(new String[]{"Some id"});
    }
     */

    @Test
    public void shouldCreateClauseToDetectPastRoutes() {
        final Pair<String, String[]> r = WhereClauses.happenedBefore(2, TimeUnit.DAYS.toMillis(5L));
        assertThat(r.first).isEqualTo(
            "EXISTS (" +
                "SELECT * FROM trips " +
                "WHERE tripGroups.uuid = trips.groupId " +
                "AND tripGroups.displayTripId = trips.id " +
                "AND trips.arrive < ?" +
                ")"
        );
        assertThat(r.second).isEqualTo(new String[]{"424800"});
    }
}
