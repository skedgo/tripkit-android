package com.skedgo.tripkit.common.model;

import org.junit.Test;
import org.junit.runner.RunWith;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import static com.skedgo.tripkit.common.model.RealTimeStatus.CAPABLE;
import static com.skedgo.tripkit.common.model.RealTimeStatus.INCAPABLE;
import static com.skedgo.tripkit.common.model.RealTimeStatus.IS_REAL_TIME;
import static com.skedgo.tripkit.common.model.RealTimeStatus.from;
import static org.assertj.core.api.Java6Assertions.assertThat;

@RunWith(AndroidJUnit4.class)
public class RealTimeStatusTest {
    @Test
    public void shouldConvertIntoCapable() {
        assertThat(from("capable")).isEqualTo(CAPABLE);
    }

    @Test
    public void shouldConvertIntoCapableEvenIfUppercaseString() {
        assertThat(from("CAPABLE")).isEqualTo(CAPABLE);
    }

    @Test
    public void shouldConvertIntoIsRealTime() {
        assertThat(from("is_real_time")).isEqualTo(IS_REAL_TIME);
    }

    @Test
    public void shouldConvertIntoIsRealTimeEvenIfUppercaseString() {
        assertThat(from("IS_REAL_TIME")).isEqualTo(IS_REAL_TIME);
    }

    @Test
    public void shouldConvertIntoIncapable() {
        assertThat(from("incapable")).isEqualTo(INCAPABLE);
    }

    @Test
    public void shouldConvertIntoIncapableEvenIfUppercaseString() {
        assertThat(from("INCAPABLE")).isEqualTo(INCAPABLE);
    }

    @Test
    public void shouldConvertIntoNullIfInvalidString() {
        assertThat(from("I'm not an enum!")).isNull();
    }
}