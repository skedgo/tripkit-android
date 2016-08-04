package com.skedgo.android.common.rx;

import com.skedgo.android.common.BuildConfig;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.Arrays;

import rx.observers.TestSubscriber;
import rx.subjects.Subject;

import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
public class VarTest {
  @Test public void shouldNotEmitValueIfNoDefaultValue() {
    final Var<String> v = Var.create();
    final TestSubscriber<String> subscriber = new TestSubscriber<>();
    v.observe().subscribe(subscriber);

    subscriber.assertNoErrors();
    assertThat(subscriber.getOnNextEvents()).isEmpty();
  }

  @Test public void shouldEmitDefaultValue() {
    final String defaultValue = "Awesome!";
    final Var<String> v = Var.create(defaultValue);
    final TestSubscriber<String> subscriber = new TestSubscriber<>();
    v.observe().subscribe(subscriber);

    subscriber.assertNoErrors();
    subscriber.assertReceivedOnNext(singletonList(defaultValue));
  }

  @Test public void valueShouldBeDefaultValue() {
    final String defaultValue = "Awesome!";
    final Var<String> v = Var.create(defaultValue);
    assertThat(v.value()).isEqualTo(defaultValue);
  }

  @Test public void valueShouldBeNullIfNoDefaultValue() {
    final Var<String> v = Var.create();
    assertThat(v.value()).isNull();
  }

  @Test public void valueShouldReflectLatestValue() {
    final Var<String> v = Var.create();
    v.put("A");
    assertThat(v.value()).isEqualTo("A");
    v.put("B");
    assertThat(v.value()).isEqualTo("B");
    v.put("C");
    assertThat(v.value()).isEqualTo("C");
  }

  @Test public void shouldEmitLatestValue() {
    final Var<String> v = Var.create();
    final TestSubscriber<String> subscriber = new TestSubscriber<>();
    v.observe().subscribe(subscriber);
    v.put("A");
    subscriber.assertReceivedOnNext(singletonList("A"));
    v.put("B");
    subscriber.assertReceivedOnNext(Arrays.asList("A", "B"));
    v.put("C");
    subscriber.assertReceivedOnNext(Arrays.asList("A", "B", "C"));
  }

  @Test public void shouldNotLeakSubject() {
    final Var<String> v = Var.create();
    assertThat(v.observe()).isNotInstanceOf(Subject.class);
  }
}