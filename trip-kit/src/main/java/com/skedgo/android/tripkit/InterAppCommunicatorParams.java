package com.skedgo.android.tripkit;

import android.content.Intent;

import com.skedgo.android.common.model.TripSegment;

import org.immutables.value.Value;

import rx.functions.Action2;

import static org.immutables.value.Value.Style.BuilderVisibility.PACKAGE;
import static org.immutables.value.Value.Style.ImplementationVisibility.PRIVATE;

@Value.Immutable
@Value.Style(visibility = PRIVATE, builderVisibility = PACKAGE)
public abstract class InterAppCommunicatorParams {

  public static Builder builder() {
    return new InterAppCommunicatorParamsBuilder();
  }

  public abstract String action();
  public abstract Action2<Integer, Intent> openApp();
  public abstract Action2<Integer, Intent> openWeb();
  public abstract TripSegment tripSegment();

  public interface Builder {
    Builder action(String action);
    Builder openApp(Action2<Integer, Intent> openApp);
    Builder openWeb(Action2<Integer, Intent> openWeb);
    Builder tripSegment(TripSegment tripSegment);

    InterAppCommunicatorParams build();
  }

}
