package com.skedgo.android.tripkit;

import android.support.annotation.Nullable;

import com.skedgo.android.common.model.TripSegment;

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
  @Nullable public abstract String flitwaysPartnerKey();

  public interface Builder {
    Builder action(String action);
    Builder segment(TripSegment segment);
    Builder flitwaysPartnerKey(String flitwaysPartnerKey);
    ExternalActionParams build();
  }
}