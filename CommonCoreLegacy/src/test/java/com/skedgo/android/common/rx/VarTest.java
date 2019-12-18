package com.skedgo.android.common.rx;

import io.reactivex.subjects.Subject;
import io.reactivex.subscribers.TestSubscriber;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import java.util.Arrays;

import static java.util.Collections.singletonList;
import static org.assertj.core.api.Java6Assertions.assertThat;

@RunWith(RobolectricTestRunner.class)
public class VarTest {
  @Test public void shouldNotEmitValueIfNoDefaultValue() {
    final Var<String> v = Var.create();
    final TestSubscriber<String> subscriber = v.observe().test();

    subscriber.assertNoErrors();
    assertThat(subscriber.getEvents().get(0)).isEmpty();
  }

  @Test public void shouldEmitDefaultValue() {
    final String defaultValue = "Awesome!";
    final Var<String> v = Var.create(defaultValue);
    final TestSubscriber<String> subscriber = v.observe().test();

    subscriber.assertNoErrors();
    subscriber.assertValueSequence(singletonList(defaultValue));
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
    subscriber.assertValueSequence(singletonList("A"));
    v.put("B");
    subscriber.assertValueSequence(Arrays.asList("A", "B"));
    v.put("C");
    subscriber.assertValueSequence(Arrays.asList("A", "B", "C"));
  }

  @Test public void shouldNotLeakSubject() {
    final Var<String> v = Var.create();
    assertThat(v.observe()).isNotInstanceOf(Subject.class);
  }
}