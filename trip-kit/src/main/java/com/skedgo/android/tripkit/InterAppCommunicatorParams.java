package com.skedgo.android.tripkit;

import org.immutables.value.Value;

import rx.functions.Action1;

import static org.immutables.value.Value.Style.BuilderVisibility.PACKAGE;
import static org.immutables.value.Value.Style.ImplementationVisibility.PRIVATE;

@Value.Immutable
@Value.Style(visibility = PRIVATE, builderVisibility = PACKAGE)
public abstract class InterAppCommunicatorParams {

  public static Builder builder() {
    return new InterAppCommunicatorParamsBuilder();
  }

  public abstract String action();
  public abstract Action1<String> openApp();
  public abstract Action1<String> openWeb();

  public interface Builder {
    Builder action(String action);
    Builder openApp(Action1<String> openApp);
    Builder openWeb(Action1<String> openWeb);

    InterAppCommunicatorParams build();
  }

}
