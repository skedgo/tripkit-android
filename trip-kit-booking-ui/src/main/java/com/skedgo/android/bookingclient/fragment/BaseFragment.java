package com.skedgo.android.bookingclient.fragment;

import android.support.v4.app.Fragment;

import rx.Observable;
import rx.subjects.PublishSubject;
import skedgo.common.view.ButterKnifeFragment;

/**
 * A base fragment that all fragments should extend. It provides 2 capabilities:
 * + Injects views via ButterKnife
 * + Propagates some lifecycle events via RxJava
 */
public class BaseFragment extends ButterKnifeFragment {
  private final PublishSubject<Fragment> onStarts = PublishSubject.create();
  private final PublishSubject<Fragment> onStops = PublishSubject.create();

  @Override
  public void onStart() {
    super.onStart();
    onStarts.onNext(this);
  }

  @Override
  public void onStop() {
    super.onStop();
    onStops.onNext(this);
  }

  public final Observable<Fragment> onStarts() {
    return onStarts;
  }

  public final Observable<Fragment> onStops() {
    return onStops;
  }
}