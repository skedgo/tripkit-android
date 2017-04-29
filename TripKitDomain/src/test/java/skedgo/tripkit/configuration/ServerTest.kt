package skedgo.tripkit.configuration

import org.assertj.core.api.Java6Assertions
import org.junit.Test

class ServerTest {
  @Test fun `Server should end with a slash`() {
    Java6Assertions.assertThat(Server.Inflationary.value).endsWith("/")
    Java6Assertions.assertThat(Server.BigBang.value).endsWith("/")
  }
}