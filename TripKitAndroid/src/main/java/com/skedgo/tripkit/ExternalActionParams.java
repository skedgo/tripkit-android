package com.skedgo.tripkit;

import androidx.annotation.Nullable;

import com.skedgo.tripkit.routing.TripSegment;

import org.immutables.value.Value;

import static org.immutables.value.Value.Style.BuilderVisibility.PACKAGE;
import static org.immutables.value.Value.Style.ImplementationVisibility.PRIVATE;

@Value.Immutable
@Value.Style(visibility = PRIVATE, builderVisibility = PACKAGE)
public abstract class ExternalActionParams {
  public static Builder builder() {
    return new ExternalActionParamsBuilder();
  }

  public abstract String action();
  public abstract TripSegment segment();
  @Nullable public abstract String flitWaysPartnerKey();

  public interface Builder {
    Builder action(String action);
    Builder segment(TripSegment segment);
    Builder flitWaysPartnerKey(String flitWaysPartnerKey);
    ExternalActionParams build();
  }
}