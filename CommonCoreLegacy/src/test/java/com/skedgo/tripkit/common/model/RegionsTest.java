package com.skedgo.tripkit.common.model;

import org.junit.Test;
import org.junit.runner.RunWith;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import static org.assertj.core.api.Java6Assertions.assertThat;

@RunWith(AndroidJUnit4.class)
public class RegionsTest {
    @Test
    public void shouldBeInstanceOfInterRegion() {
        final Region r = Regions.createInterRegion(new Region(), new Region());
        assertThat(r).isInstanceOf(Regions.InterRegion.class);
    }

    @Test
    public void shouldNotEqual() {
        assertThat(Regions.equals(new Region(), null)).isFalse();
        assertThat(Regions.equals(null, new Region())).isFalse();
        final Region a = new Region();
        a.setName("A");
        final Region b = new Region();
        b.setName("B");
        assertThat(Regions.equals(a, b)).isFalse();
    }

    @Test
    public void shouldEqual() {
        assertThat(Regions.equals(null, null)).isTrue();
        final Region a = new Region();
        a.setName("R");
        final Region b = new Region();
        b.setName("R");
        assertThat(Regions.equals(a, b)).isTrue();
    }
}