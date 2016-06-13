package com.skedgo.android.bookingclient.viewmodel;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.View;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;

public final class TakeSnapshotCommand implements Command<FragmentActivity, Bitmap> {
  @Inject public TakeSnapshotCommand() {}

  @Override
  public Observable<Bitmap> executeAsync(final FragmentActivity activity) {
    return Observable.create(new Observable.OnSubscribe<Bitmap>() {
      @Override
      public void call(final Subscriber<? super Bitmap> subscriber) {
        List<Fragment> fragmentList = activity.getSupportFragmentManager().getFragments();
        SupportMapFragment mapFragment = null;
        for (Fragment fragment : fragmentList) {
          if (fragment instanceof SupportMapFragment) {
            mapFragment = (SupportMapFragment) fragment;
            break;
          }
        }
        if (mapFragment != null) {
          final Fragment fragment = mapFragment;
          mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override public void onMapReady(GoogleMap googleMap) {
              final View mapView = fragment.getView();
              googleMap.snapshot(new GoogleMap.SnapshotReadyCallback() {
                @Override
                public void onSnapshotReady(Bitmap mapSnapshot) {
                  if (mapSnapshot != null) {
                    int[] mapViewLocation = new int[2];
                    mapView.getLocationOnScreen(mapViewLocation);

                    Bitmap activitySnapshot = takeActivitySnapshot(activity);
                    Bitmap finalSnapshot = combineSnapshots(activitySnapshot, mapSnapshot, mapViewLocation);
                    subscriber.onNext(finalSnapshot);

                    // Dispose unused snapshots.
                    mapSnapshot.recycle();
                    activitySnapshot.recycle();
                  } else {
                    Bitmap activitySnapshot = takeActivitySnapshot(activity);
                    subscriber.onNext(activitySnapshot);
                  }

                  subscriber.onCompleted();
                }
              });
            }
          });
        } else {
          Bitmap activitySnapshot = takeActivitySnapshot(activity);
          subscriber.onNext(activitySnapshot);
          subscriber.onCompleted();
        }
      }
    });
  }

  private Bitmap takeActivitySnapshot(FragmentActivity activity) {
    View decorView = activity.getWindow().getDecorView();
    Bitmap activitySnapshot = Bitmap.createBitmap(
        decorView.getWidth(),
        decorView.getHeight(),
        Bitmap.Config.ARGB_8888
    );

    Canvas canvas = new Canvas(activitySnapshot);
    decorView.draw(canvas);
    return activitySnapshot;
  }

  private Bitmap combineSnapshots(Bitmap activitySnapshot, Bitmap mapSnapshot, int[] mapViewLocationOnScreen) {
    Bitmap finalSnapshot = Bitmap.createBitmap(
        activitySnapshot.getWidth(),
        activitySnapshot.getHeight(),
        activitySnapshot.getConfig()
    );

    Canvas canvas = new Canvas(finalSnapshot);
    canvas.drawBitmap(mapSnapshot, mapViewLocationOnScreen[0], mapViewLocationOnScreen[1], null);
    canvas.drawBitmap(activitySnapshot, 0, 0, null);
    canvas.setDensity(72);
    return finalSnapshot;
  }
}