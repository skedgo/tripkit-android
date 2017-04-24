package com.skedgo.android.tripkit;

import android.content.Context;
import android.support.annotation.NonNull;

import com.google.android.gms.gcm.GcmNetworkManager;
import com.google.android.gms.gcm.GcmTaskService;
import com.google.android.gms.gcm.OneoffTask;
import com.google.android.gms.gcm.Task;
import com.google.android.gms.gcm.TaskParams;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Actions;
import skedgo.tripkit.android.TripKit;

public final class FetchRegionsService extends GcmTaskService {
  public static Observable<Void> scheduleAsync(@NonNull final Context context) {
    return Observable.create(new Observable.OnSubscribe<Void>() {
      @Override public void call(Subscriber<? super Void> subscriber) {
        final OneoffTask task = new OneoffTask.Builder()
            .setService(FetchRegionsService.class)
            .setTag("FetchRegions")
            .setExecutionWindow(0, TimeUnit.MINUTES.toSeconds(1))
            .setPersisted(true)
            .setUpdateCurrent(true)
            .setRequiredNetwork(Task.NETWORK_STATE_CONNECTED)
            .setRequiresCharging(true)
            .build();
        GcmNetworkManager.getInstance(context.getApplicationContext())
            .schedule(task);
        subscriber.onCompleted();
      }
    });
  }

  @Override public int onRunTask(TaskParams taskParams) {
    final TripKit kit = TripKit.getInstance();
    try {
      kit.getRegionService()
          .refreshAsync()
          .toBlocking()
          .forEach(Actions.empty());
      return GcmNetworkManager.RESULT_SUCCESS;
    } catch (Exception e) {
      kit.getErrorHandler().call(e);
      return GcmNetworkManager.RESULT_RESCHEDULE;
    }
  }
}