package com.skedgo.routepersistence;

import android.util.Pair;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 22)
public class GroupQueriesTest {
  @Test public void shouldCreateRawQueryToMatchNotifiableRoutes() {
    final Pair<String, String[]> r = GroupQueries.isNotifiable();
    assertThat(r.first).isEqualTo("select * from tripGroups where isNotifiable = 1");
    assertThat(r.second).isNull();
  }

  @Test public void shouldCreateRawQueryToMatchCorrectRequestId() {
    final Pair<String, String[]> r = GroupQueries.hasRequestId("Some id");
    assertThat(r.first).isEqualTo("select * from routes where route_id = ?");
    assertThat(r.second).containsExactly("Some id");
  }

  @Test public void shouldCreateRawQueryToMatchCorrectRouteByUuid() {
    final Pair<String, String[]> r = GroupQueries.hasUuid("Some id");
    assertThat(r.first).isEqualTo("select * from tripGroups where uuid = ?");
    assertThat(r.second).containsExactly("Some id");
  }
}
