package com.skedgo.android.tripkit.booking;

import android.support.annotation.Nullable;

import com.google.gson.annotations.SerializedName;

import org.immutables.gson.Gson;
import org.immutables.value.Value;

@Gson.TypeAdapters
@Value.Immutable
public class AccessToken {

  @SerializedName("access_token") private String accessToken;
  @SerializedName("token_type") private String tokenType;
  @SerializedName("expires_in") private int expiresIn;
  @Nullable @SerializedName("refresh_token") private String refreshToken;

  public String getAccessToken() {
    return accessToken;
  }

  @Nullable public String getRefreshToken() {
    return refreshToken;
  }

  public int getExpiresIn() {
    return expiresIn;
  }

  public String getTokenType() {
    // OAuth requires uppercase Authorization HTTP header value for token type
    if ( ! Character.isUpperCase(tokenType.charAt(0))) {
      tokenType =
          Character
              .toString(tokenType.charAt(0))
              .toUpperCase() + tokenType.substring(1);
    }

    return tokenType;
  }
}
