package com.skedgo.tripkit.a2brouting;

import com.google.gson.Gson;
import com.skedgo.tripkit.RoutingUserError;

import io.reactivex.observers.TestObserver;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.robolectric.RobolectricTestRunner;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import androidx.test.core.app.ApplicationProvider;
import io.reactivex.Observable;
import com.skedgo.tripkit.routing.RoutingResponse;
import com.skedgo.tripkit.routing.TripGroup;

import static org.mockito.Matchers.anyListOf;
import static org.mockito.Matchers.anyMapOf;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(RobolectricTestRunner.class)
public class FailoverA2bRoutingApiTest {
  @Rule public MockitoRule rule = MockitoJUnit.rule();
  @Mock
  A2bRoutingApi api;
  private FailoverA2bRoutingApi failoverA2bRoutingApi;

  @Before public void before() {
    failoverA2bRoutingApi = new FailoverA2bRoutingApi(
        ApplicationProvider.getApplicationContext().getResources(),
        new Gson(),
        api
    );
  }

  @Test public void shouldFailSilentlyIfMissingUrls() {
    final TestObserver<List<TripGroup>> subscriber = new TestObserver<>();
    failoverA2bRoutingApi.fetchRoutesAsync(
        Collections.<String>emptyList(),
        Collections.<String>emptyList(),
        Collections.<String>emptyList(),
        Collections.<String>emptyList(),
        Collections.<String, Object>emptyMap()
    ).subscribe(subscriber);

    subscriber.awaitTerminalEvent();
    subscriber.assertNoErrors();
  }

  @Test public void shouldFailSilentlyIfAllRequestsFail() {
    when(api.execute(
        anyString(),
        anyListOf(String.class),
        anyListOf(String.class),
        anyListOf(String.class),
        anyMapOf(String.class, Object.class)
    )).thenThrow(new RuntimeException());

    final TestObserver<List<TripGroup>> subscriber = new TestObserver<>();
    failoverA2bRoutingApi.fetchRoutesAsync(
        Arrays.asList("https://www.abc.com", "https://www.def.com"),
        Arrays.asList("hyperloop", "drone"),
        Collections.<String>emptyList(),
        Collections.<String>emptyList(),
        Collections.<String, Object>emptyMap()
    ).subscribe(subscriber);

    subscriber.awaitTerminalEvent();
    subscriber.assertNoErrors();
  }

  @Test public void shouldFailSilentlyIfNoTripGroupsFoundOnAllUrls() {
    final RoutingResponse response = new RoutingResponse();
    when(api.execute(
        anyString(),
        anyListOf(String.class),
        anyListOf(String.class),
        anyListOf(String.class),
        anyMapOf(String.class, Object.class)
    )).thenReturn(Observable.just(response));

    final TestObserver<List<TripGroup>> subscriber = new TestObserver<>();
    failoverA2bRoutingApi.fetchRoutesAsync(
        Arrays.asList("https://www.abc.com", "https://www.def.com"),
        Arrays.asList("hyperloop", "drone"),
        Collections.<String>emptyList(),
        Collections.<String>emptyList(),
        Collections.<String, Object>emptyMap()
    ).subscribe(subscriber);

    subscriber.awaitTerminalEvent();
    subscriber.assertNoErrors();
  }

  @Test public void throwUserErrorWhenRoutingWithMultipleUrls() {
    final RoutingResponse response = mock(RoutingResponse.class);
    when(response.getErrorMessage()).thenReturn("Some user error");
    when(response.hasError()).thenReturn(true);
    when(api.execute(
        eq("https://www.abc.com/routing.json"),
        anyListOf(String.class),
        anyListOf(String.class),
        anyListOf(String.class),
        anyMapOf(String.class, Object.class)
    )).thenReturn(Observable.just(response));

    final TestObserver<List<TripGroup>> subscriber = new TestObserver<>();
    failoverA2bRoutingApi.fetchRoutesAsync(
        Arrays.asList("https://www.abc.com", "https://www.def.com"),
        Collections.<String>emptyList(),
        Collections.<String>emptyList(),
        Collections.<String>emptyList(),
        Collections.<String, Object>emptyMap()
    ).subscribe(subscriber);

    subscriber.awaitTerminalEvent();
    subscriber.assertError(RoutingUserError.class);
    subscriber.assertNoValues();
  }

  @Test public void shouldThrowUserError() {
    final RoutingResponse response = mock(RoutingResponse.class);
    when(response.getErrorMessage()).thenReturn("Some user error");
    when(response.hasError()).thenReturn(true);
    when(api.execute(
        eq("Link 0"),
        anyListOf(String.class),
        anyListOf(String.class),
        anyListOf(String.class),
        anyMapOf(String.class, Object.class)
    )).thenReturn(Observable.just(response));

    final TestObserver<RoutingResponse> subscriber = new TestObserver<>();
    failoverA2bRoutingApi.fetchRoutesPerUrlAsync(
        "Link 0",
        Collections.<String>emptyList(),
        Collections.<String>emptyList(),
        Collections.<String>emptyList(),
        Collections.<String, Object>emptyMap()
    ).subscribe(subscriber);

    subscriber.awaitTerminalEvent();
    subscriber.assertError(RoutingUserError.class);
    subscriber.assertNoValues();
  }

  @Test public void shouldEmitNothingIfHavingErrorAndNotUserError() {
    final RoutingResponse response = mock(RoutingResponse.class);
    when(response.getErrorMessage()).thenReturn("Some error");
    when(response.hasError()).thenReturn(false);
    when(api.execute(
        eq("Link 0"),
        anyListOf(String.class),
        anyListOf(String.class),
        anyListOf(String.class),
        anyMapOf(String.class, Object.class)
    )).thenReturn(Observable.just(response));

    final TestObserver<RoutingResponse> subscriber = new TestObserver<>();
    failoverA2bRoutingApi.fetchRoutesPerUrlAsync(
        "Link 0",
        Collections.<String>emptyList(),
        Collections.<String>emptyList(),
        Collections.<String>emptyList(),
        Collections.<String, Object>emptyMap()
    ).subscribe(subscriber);

    subscriber.awaitTerminalEvent();
    subscriber.assertNoErrors();
    subscriber.assertNoValues();
  }
}