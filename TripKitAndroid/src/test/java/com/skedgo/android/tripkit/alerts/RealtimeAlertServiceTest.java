package com.skedgo.android.tripkit.alerts;

import com.skedgo.android.tripkit.TestRunner;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import java.util.Arrays;
import java.util.List;

import rx.Observable;
import rx.exceptions.CompositeException;
import rx.observers.TestSubscriber;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


public class RealtimeAlertServiceTest {
  @Rule public final MockitoRule rule = MockitoJUnit.rule();
  @Mock RealtimeAlertApi api;
  private RealtimeAlertService service;

  @Before public void before() {
    service = new RealtimeAlertService(api);
  }

  /**
   * We manage to fetch via first server, then we ignore second server.
   */
  @Test public void fetchRealtimeAlertsSuccessfully() {
    final List<String> baseUrls = Arrays.asList(
        "http://tripgo.com/",
        "http://riogo.com/"
    );
    final RealtimeAlertResponse response = ImmutableRealtimeAlertResponse.builder().build();
    when(api.fetchRealtimeAlertsAsync(
        eq("http://tripgo.com/alerts/transit.json"),
        eq("sydney")
    )).thenReturn(Observable.just(response));

    final TestSubscriber<RealtimeAlertResponse> subscriber = new TestSubscriber<>();
    service.fetchRealtimeAlertsAsync(baseUrls, "sydney").subscribe(subscriber);
    subscriber.awaitTerminalEvent();
    subscriber.assertNoErrors();
    subscriber.assertValue(response);

    verify(api).fetchRealtimeAlertsAsync(
        eq("http://tripgo.com/alerts/transit.json"),
        eq("sydney")
    );
  }

  /**
   * When we fail to fetch via first server but manage via second server.
   */
  @Test public void fetchRealtimeAlertsSuccessfullyVia2ndServer() {
    final List<String> baseUrls = Arrays.asList(
        "http://tripgo.com/",
        "http://riogo.com/"
    );
    final RealtimeAlertResponse response = ImmutableRealtimeAlertResponse.builder().build();
    final RuntimeException error = new RuntimeException("1st server is down");
    when(api.fetchRealtimeAlertsAsync(anyString(), eq("sydney")))
        .thenReturn(Observable.<RealtimeAlertResponse>error(error))
        .thenReturn(Observable.just(response));

    final TestSubscriber<RealtimeAlertResponse> subscriber = new TestSubscriber<>();
    service.fetchRealtimeAlertsAsync(baseUrls, "sydney").subscribe(subscriber);
    subscriber.awaitTerminalEvent();
    subscriber.assertNoErrors();
    subscriber.assertValue(response);

    verify(api, times(2)).fetchRealtimeAlertsAsync(
        anyString(),
        eq("sydney")
    );
  }

  /**
   * When we fail to fetch via both servers.
   */
  @Test public void failToFetchRealtimeAlerts() {
    final List<String> baseUrls = Arrays.asList(
        "http://tripgo.com/",
        "http://riogo.com/"
    );
    final RuntimeException firstError = new RuntimeException("1st server is down");
    final RuntimeException secondError = new RuntimeException("2nd server is down");
    when(api.fetchRealtimeAlertsAsync(anyString(), eq("sydney")))
        .thenReturn(Observable.<RealtimeAlertResponse>error(firstError))
        .thenReturn(Observable.<RealtimeAlertResponse>error(secondError));

    final TestSubscriber<RealtimeAlertResponse> subscriber = new TestSubscriber<>();
    service.fetchRealtimeAlertsAsync(baseUrls, "sydney").subscribe(subscriber);
    subscriber.awaitTerminalEvent();
    subscriber.assertError(CompositeException.class);

    verify(api, times(2)).fetchRealtimeAlertsAsync(
        anyString(),
        eq("sydney")
    );
  }
}