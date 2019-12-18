package com.skedgo.tripkit.common.rx;


import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.functions.Consumer;
import io.reactivex.subjects.BehaviorSubject;

/**
 * Represents a mutable property that can be observed via {@link Observable}.
 * <p>
 * Use {@link BehaviorSubject} instead.
 */
@Deprecated
public abstract class Var<T> implements Consumer<T> {
  public static <T> Var<T> create() {
    return new Impl<T>();
  }

  public static <T> Var<T> create(T defaultValue) {
    return new Impl<T>(defaultValue);
  }

  public abstract T value();
  public abstract void put(T value);
  public abstract boolean hasValue();
  public abstract Flowable<T> observe();

  private static class Impl<T> extends Var<T> {
    private final BehaviorSubject<T> subject;
    private T value;

    Impl() {
      subject = BehaviorSubject.create();
    }

    Impl(T defaultValue) {
      this.value = defaultValue;
      subject = BehaviorSubject.createDefault(defaultValue);
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

    @Override public Flowable<T> observe() {
      return subject.toFlowable(BackpressureStrategy.BUFFER);
    }

    @Override
    public void accept(T t) throws Exception {
      put(value);
    }
  }
}