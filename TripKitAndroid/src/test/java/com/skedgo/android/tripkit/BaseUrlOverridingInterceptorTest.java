package com.skedgo.android.tripkit;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatcher;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricTestRunner;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import rx.functions.Func0;

import static org.mockito.Matchers.argThat;
import static org.mockito.Matchers.same;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(RobolectricTestRunner.class)
public class BaseUrlOverridingInterceptorTest {
  @Mock Func0<String> baseUrlAdapter;
  private BaseUrlOverridingInterceptor interceptor;

  @Before public void before() {
    MockitoAnnotations.initMocks(this);
    interceptor = new BaseUrlOverridingInterceptor(baseUrlAdapter);
  }

  @Test public void overrideSatappRequestWithoutQueryParams() throws IOException {
    when(baseUrlAdapter.call()).thenReturn("https://granduni.buzzhives.com/satapp-beta/");

    final Interceptor.Chain chain = mock(Interceptor.Chain.class);
    final Request chainRequest = new Request.Builder()
        .url("https://sydney-au-nsw-sydney.tripgo.skedgo.com/satapp/regions.json")
        .build();
    when(chain.request()).thenReturn(chainRequest);

    final Request expectedRequest = chainRequest.newBuilder()
        .url("https://granduni.buzzhives.com/satapp-beta/regions.json")
        .build();
    interceptor.intercept(chain);

    verify(chain).proceed(argThat(new ArgumentMatcher<Request>() {
      @Override public boolean matches(Request actualRequest) {
        return actualRequest.url().equals(expectedRequest.url())
            && actualRequest.method().equals(expectedRequest.method());
      }
    }));
  }

  @Test public void overrideSatappRequestWithQueryParams() throws IOException {
    when(baseUrlAdapter.call()).thenReturn("https://granduni.buzzhives.com/satapp-beta/");

    final Interceptor.Chain chain = mock(Interceptor.Chain.class);
    final Request chainRequest = new Request.Builder()
        .url(HttpUrl.parse("https://lepton-us-co-denver.tripgo.skedgo.com/satapp/routing.json?modes=ps_tax&v=11&arriveBefore=0&tt=0&departAfter=1459485056&version=a-beta4.5.1-debug"))
        .build();
    when(chain.request()).thenReturn(chainRequest);

    final Request expectedRequest = chainRequest.newBuilder()
        .url(HttpUrl.parse("https://granduni.buzzhives.com/satapp-beta/routing.json?modes=ps_tax&v=11&arriveBefore=0&tt=0&departAfter=1459485056&version=a-beta4.5.1-debug"))
        .build();
    interceptor.intercept(chain);

    verify(chain).proceed(argThat(new ArgumentMatcher<Request>() {
      @Override public boolean matches(Request request) {
        return request.url().equals(expectedRequest.url())
            && request.method().equals(expectedRequest.method());
      }
    }));
  }

  @Test public void ignoreNonTripgoRequest() throws IOException {
    when(baseUrlAdapter.call()).thenReturn("https://granduni.buzzhives.com/satapp-beta/");

    final Interceptor.Chain chain = mock(Interceptor.Chain.class);
    final Request chainRequest = new Request.Builder()
        .url(HttpUrl.parse("https://google.com/haha"))
        .build();
    when(chain.request()).thenReturn(chainRequest);

    interceptor.intercept(chain);
    verify(chain).proceed(same(chainRequest));
  }

  @Test public void ignoreIfNoNewBaseUrlAvailable() throws IOException {
    when(baseUrlAdapter.call()).thenReturn(null);

    final Interceptor.Chain chain = mock(Interceptor.Chain.class);
    final Request chainRequest = new Request.Builder()
        .url(HttpUrl.parse("https://skedgo.com/tripgo"))
        .build();
    when(chain.request()).thenReturn(chainRequest);

    interceptor.intercept(chain);
    verify(chain).proceed(same(chainRequest));
  }
}