package com.skedgo.android.tripkit;

import android.content.Context;
import android.support.annotation.Nullable;

import org.immutables.value.Value;

import java.util.List;

import rx.functions.Action1;
import rx.functions.Func1;

import static org.immutables.value.Value.Style.BuilderVisibility.PACKAGE;
import static org.immutables.value.Value.Style.ImplementationVisibility.PRIVATE;

@Value.Immutable
@Value.Style(visibility = PRIVATE, builderVisibility = PACKAGE)
public abstract class Configs {
  public static Builder builder() {
    return new ConfigsBuilder();
  }

  public abstract Context context();
  public abstract String regionEligibility();
  public abstract boolean debuggable();
  @Nullable public abstract Action1<Throwable> errorHandler();
  public abstract Func1<String, List<String>> avoidModesLoader();

  public interface Builder {
    Builder context(Context context);
    Builder regionEligibility(String regionEligibility);
    Builder debuggable(boolean debuggable);
    Builder errorHandler(Action1<Throwable> errorHandler);
    Builder avoidModesLoader(Func1<String, List<String>> avoidModesLoader);
    Configs build();
  }
}