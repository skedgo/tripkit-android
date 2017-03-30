package skedgo.tripkit.routing.a2b;

import org.immutables.value.Value;
import org.immutables.value.Value.Style.ImplementationVisibility;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import kotlin.Pair;

@Value.Immutable
@Value.Style(visibility = ImplementationVisibility.PACKAGE)
public abstract class A2bRoutingRequest {
  @NotNull public static Builder builder() {
    return null;
  }

  public abstract @NotNull Pair<Double, Double> origin();
  public abstract @NotNull Pair<Double, Double> destination();
  public abstract @Nullable String originAddress();
  public abstract @Nullable String destinationAddress();

  public interface Builder {
    Builder origin(@NotNull Pair<Double, Double> origin);
    Builder destination(@NotNull Pair<Double, Double> destination);
    Builder originAddress(@Nullable String originAddress);
    Builder destinationAddress(@Nullable String destinationAddress);
    A2bRoutingRequest build();
  }
}
