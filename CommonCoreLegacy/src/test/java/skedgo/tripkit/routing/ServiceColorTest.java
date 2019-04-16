package skedgo.tripkit.routing;

import com.skedgo.android.common.TestRunner;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.assertj.core.api.Java6Assertions.assertThat;

@RunWith(TestRunner.class)
public class ServiceColorTest {
  @Test
  public void shouldEqual() {
    assertThat(new ServiceColor(1, 2, 3)).isEqualTo(new ServiceColor(1, 2, 3));
  }

  @Test
  public void shouldNotEqual() {
    assertThat(new ServiceColor(1, 2, 3)).isNotEqualTo(new ServiceColor(0, 2, 3));
    assertThat(new ServiceColor(1, 2, 3)).isNotEqualTo(new ServiceColor(1, 0, 3));
    assertThat(new ServiceColor(1, 2, 3)).isNotEqualTo(new ServiceColor(1, 2, 0));
    assertThat(new ServiceColor(1, 2, 3)).isNotEqualTo("Awesome!");
  }
}