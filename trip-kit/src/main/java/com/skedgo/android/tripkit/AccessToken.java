package com.skedgo.android.tripkit;

import com.google.gson.annotations.SerializedName;

import org.immutables.gson.Gson;
import org.immutables.value.Value;

@Gson.TypeAdapters
@Value.Immutable
public class AccessToken {

  @SerializedName("accessToken") private String accessToken;
  @SerializedName("tokenType") private String tokenType;

  public String getAccessToken() {
    return accessToken;
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
