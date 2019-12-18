package com.skedgo.android.tripkit.booking;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import io.reactivex.observers.TestObserver;
import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.nio.charset.Charset;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class ExternalOAuthApiTest {

  private ExternalOAuthApi externalOAuthApi;
  private MockWebServer mockWebServer;

  @Before
  public void setUp() throws Exception {
    mockWebServer = new MockWebServer();
    try {
      mockWebServer.start();
    } catch (IOException e) {
      e.printStackTrace();
    }

    final Gson gson = new GsonBuilder()
        .registerTypeAdapterFactory(new GsonAdaptersAccessTokenResponse())
        .create();

    externalOAuthApi = new Retrofit.Builder()
        .baseUrl(mockWebServer.url(""))
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()
        .create(ExternalOAuthApi.class);
  }

  @Test public void getTokenAsync() throws IOException {
    mockWebServer.enqueue(new MockResponse().setBody(IOUtils.toString(getClass().
        getResourceAsStream("/oauth2-token.json"), Charset.defaultCharset())));

    AccessTokenResponse attractionsResponse = ImmutableAccessTokenResponse.builder()
        .accessToken("SANDBOX-gAAAAABXaBqBJ0daKhV6Lix6E5tNMKIJK9jgdlNqK-v19KP4aZxoXqcU8OD8Of2GX1GL39QBwm54JCZ24VxkhOQOaMIfWaS4aKELelaV5YFTx3emBbVDJcdNxrccg8zlNAPY6shO8AVSkjMA5yinxhnk8SGoGclccNgCeFTzXmfWQLdovUk44Lr2S1KJWtIUB6OMbXG2wLeXzNxweEvWjBhuBhbKFGh5afijbWoSbLWO4CFL-bFik4EdohBKbWZW2Cv-7vyai-KCpCO8ssHemmAAXYpW3oJYRzUpobermaYIrLsRerUsacxFCR0_RrM9WzbAKGtBdg-Y")
        .expiresIn(3600)
        .refreshToken("6VvN1u66GZr3ONVG_Lv7UUStrdrpeBMbgu2kXUVBJjix22DrqJDi4XOIYVtzHVFP")
        .tokenType("Bearer")
        .build();

    TestObserver<AccessTokenResponse> subscriber = externalOAuthApi.getAccessToken("", "", "", "", "", "")
        .test();
    subscriber.awaitTerminalEvent();

    subscriber.assertNoErrors();
    subscriber.assertValue(attractionsResponse);

  }
}