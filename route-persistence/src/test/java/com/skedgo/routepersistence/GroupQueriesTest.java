package com.skedgo.routepersistence;

import android.util.Pair;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import static org.assertj.core.api.Java6Assertions.assertThat;

@RunWith(RobolectricTestRunner.class)
public class GroupQueriesTest {
    @Test
    public void shouldCreateRawQueryToMatchNotifiableRoutes() {
        final Pair<String, String[]> r = GroupQueries.INSTANCE.isNotifiable();
        assertThat(r.first).isEqualTo("select * from tripGroups where isNotifiable = 1");
        assertThat(r.second).isNull();
    }

    @Test
    public void shouldCreateRawQueryToMatchCorrectRequestId() {
        final Pair<String, String[]> r = GroupQueries.INSTANCE.hasRequestId("Some id");
        assertThat(r.first).isEqualTo("select * from tripGroups where requestId = ?");
        assertThat(r.second).containsExactly("Some id");
    }

    @Test
    public void shouldCreateRawQueryToMatchCorrectRouteByUuid() {
        final Pair<String, String[]> r = GroupQueries.INSTANCE.hasUuid("Some id");
        assertThat(r.first).isEqualTo("select * from tripGroups where uuid = ?");
        assertThat(r.second).containsExactly("Some id");
    }
}
