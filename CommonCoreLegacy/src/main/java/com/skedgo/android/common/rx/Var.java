package com.skedgo.android.common.rx;

import rx.Observable;
import rx.functions.Action1;
import rx.subjects.BehaviorSubject;

/**
 * Represents a mutable property that can be observed via {@link Observable}.
 */
public abstract class Var<T> implements Action1<T> {
  public static <T> Var<T> create() {
    return new Impl<T>();
  }

  public static <T> Var<T> create(T defaultValue) {
    return new Impl<T>(defaultValue);
  }

  public abstract T value();
  public abstract void put(T value);
  public abstract boolean hasValue();
  public abstract Observable<T> observe();

  private static class Impl<T> extends Var<T> {
    private final BehaviorSubject<T> subject;
    private T value;

    Impl() {
      subject = BehaviorSubject.create();
    }

    Impl(T defaultValue) {
      this.value = defaultValue;
      subject = BehaviorSubject.create(defaultValue);
    }

    @Override public T value() {
      return value;
    }

    @Override public void put(T value) {
      this.value = value;
      subject.onNext(value);
    }

    @Override public boolean hasValue() {
      return value != null;
    }

    @Override public Observable<T> observe() {
      return subject.asObservable();
    }

    @Override public void call(T value) {
      put(value);
    }
  }
}