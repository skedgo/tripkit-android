package com.skedgo.android.common.util;

import android.content.Context;
import android.support.annotation.NonNull;

import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.Protocol;
import com.squareup.okhttp.Response;
import com.squareup.okhttp.ResponseBody;

import org.assertj.core.api.Assertions;

import java.io.IOException;
import java.io.InputStream;

import okio.Buffer;
import okio.BufferedSource;

public final class MockResponseInterceptor implements Interceptor {
  private final Context context;
  private final String assetFileName;

  private MockResponseInterceptor(@NonNull Context context,
                                  @NonNull String assetFileName) {
    this.context = context;
    this.assetFileName = assetFileName;
  }

  @NonNull
  public static MockResponseInterceptor create(@NonNull Context context,
                                               @NonNull String assetFileName) {
    return new MockResponseInterceptor(
        context,
        assetFileName
    );
  }

  @Override
  public Response intercept(Chain chain) throws IOException {
    return new Response.Builder()
        .protocol(Protocol.HTTP_2)
        .code(200)
        .request(chain.request())
        .body(new ResponseBody() {
          @Override
          public MediaType contentType() {
            return null;
          }

          @Override
          public long contentLength() {
            return -1;
          }

          @Override
          public BufferedSource source() {
            try {
              final InputStream stream = context.getAssets().open(assetFileName);
              return new Buffer().readFrom(stream);
            } catch (IOException e) {
              // Fail early!
              Assertions.fail(e.getMessage(), e.getCause());
              return null;
            }
          }
        })
        .build();
  }
}