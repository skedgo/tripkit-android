package skedgo.tripkit.configuration

import org.assertj.core.api.Java6Assertions.assertThat
import org.junit.Test

class ServerTest {
  @Test fun `Server should end with a slash`() {
    assertThat(Server.Inflationary.value).endsWith("/")
    assertThat(Server.BigBang.value).endsWith("/")
  }
}