package skedgo.tripkit.a2brouting;

import androidx.annotation.NonNull;

import com.skedgo.android.common.model.Query;
import com.skedgo.android.tripkit.ModeFilter;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import rx.Observable;
import rx.subjects.PublishSubject;
import rx.subjects.Subject;
import skedgo.tripkit.routing.TripGroup;

/**
 * A decorator of {@link RouteService} that performs only one routing request.
 * For example, if we ask it to route from A to B, and while that request
 * is still progress and later we ask it to route from C to B,
 * then the request A-to-B will be cancelled.
 * Cancellation is invoked asynchronously. That means the execution
 * of the request C-to-D doesn't have to wait for
 * the cancellation of the request A-to-B to be done to get started.
 */
public class SingleRouteService implements RouteService {
  /* toSerialized() to be thread-safe. */
  private final Subject<Void, Void> cancellationSignal =
      PublishSubject.<Void>create().toSerialized();
  private final RouteService routeService;

  public SingleRouteService(RouteService routeService) {
    this.routeService = routeService;
  }

  @NotNull @Override
  public Observable<List<TripGroup>> routeAsync(@NotNull Query query, @NotNull ModeFilter modeFilter) {
    cancellationSignal.onNext(null);
    return routeService.routeAsync(query, modeFilter)
        .takeUntil(cancellationSignal.asObservable());
  }
}
