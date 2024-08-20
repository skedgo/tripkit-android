package com.skedgo.tripkit;

import com.skedgo.tripkit.common.util.Gsons;
import com.skedgo.tripkit.routing.RoutingResponse;

import org.apache.commons.io.IOUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Collections;

import io.reactivex.observers.TestObserver;
import okhttp3.HttpUrl;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

import static org.assertj.core.api.Java6Assertions.assertThat;

public class TemporaryUrlApiTest extends TripKitAndroidRobolectricTest {
    private MockWebServer server;
    private TemporaryUrlApi api;
    private HttpUrl baseUrl;

    @Before
    public void before() {
        server = new MockWebServer();
        baseUrl = server.url("/");
        api = new Retrofit.Builder()
            .baseUrl(baseUrl)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(Gsons.createForLowercaseEnum()))
            .build()
            .create(TemporaryUrlApi.class);
    }

    @After
    public void after() throws IOException {
        server.shutdown();
    }

    @Test
    public void fetchTripSuccessfully() throws IOException {
        final MockResponse mockResponse = new MockResponse();
        mockResponse.setResponseCode(200);
        mockResponse.setBody(IOUtils.toString(
            getClass().getResourceAsStream("/temporaryURL.json"),
            Charset.defaultCharset()
        ));
        server.enqueue(mockResponse);

        final HttpUrl url = baseUrl.newBuilder()
            .addPathSegments("trip/0a1cba21-f177-4706-bbb8-ebd8057e5f4f")
            .build();
        final TestObserver<RoutingResponse> subscriber = api.requestTemporaryUrlAsync(
            url.toString(),
            Collections.<String, Object>emptyMap()
        ).test();

        final RoutingResponse response = subscriber.values().get(0);
        assertThat(response.getTripGroupList()).isNotEmpty();
    }
}