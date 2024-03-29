package com.skedgo.tripkit.account.data;

import com.google.gson.annotations.JsonAdapter;

import static org.immutables.gson.Gson.TypeAdapters;
import static org.immutables.value.Value.Immutable;
import static org.immutables.value.Value.Style;

@TypeAdapters
@Immutable
@Style(passAnnotations = JsonAdapter.class)
@JsonAdapter(GsonAdaptersSignUpBody.class)
abstract class SignUpBody {
  public abstract String password();
  public abstract String username();
}
