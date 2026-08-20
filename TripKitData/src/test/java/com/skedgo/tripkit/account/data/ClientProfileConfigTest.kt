package com.skedgo.tripkit.account.data

import com.google.gson.Gson
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class ClientProfileConfigTest {

    private val gson = Gson()

    @Test
    fun `decodes rider categories description and SMS disclaimer`() {
        val client = gson.fromJson(
            """
            {
              "clientID": "1409622924572",
              "clientName": "Waupaca beta",
              "profile": {
                "riderCategories": [{
                  "id": "veteran",
                  "displayName": "Veteran",
                  "translations": {"es": "Veterano"}
                }]
              },
              "uiConfig": {
                "categoryDescription": {
                  "text": "Select all that apply.",
                  "translations": {"es": "Seleccione todas las que correspondan."}
                },
                "messages": {
                  "smsDisclaimer": {
                    "text": "By providing your phone number, you agree to receive SMS messages.",
                    "translations": {"es": "Al proporcionar su número, acepta recibir mensajes SMS."}
                  }
                }
              }
            }
            """.trimIndent(),
            Client::class.java
        )

        assertThat(client.riderCategories.map { it.id }).containsExactly("veteran")
        assertThat(client.riderCategories.single().translations?.get("es")).isEqualTo("Veterano")
        assertThat(client.categoryDescription?.text).isEqualTo("Select all that apply.")
        assertThat(client.smsDisclaimer?.translations?.get("es"))
            .isEqualTo("Al proporcionar su número, acepta recibir mensajes SMS.")
    }

    @Test
    fun `legacy client without profile configuration stays empty`() {
        val client = gson.fromJson(
            """{"clientID":"1","clientName":"Legacy","features":["WALLET"]}""",
            Client::class.java
        )

        assertThat(client.riderCategories).isEmpty()
        assertThat(client.categoryDescription).isNull()
        assertThat(client.smsDisclaimer).isNull()
        assertThat(client.hasWalletFeature()).isTrue()
    }

    @Test
    fun `partial category configuration uses safe defaults`() {
        val client = gson.fromJson(
            """{"clientID":"1","clientName":"Client","profile":{"riderCategories":[{"id":"disabled"}]},"uiConfig":{"categoryDescription":{}}}""",
            Client::class.java
        )

        assertThat(client.riderCategories.single().displayName).isEmpty()
        assertThat(client.riderCategories.single().translations).isEmpty()
        assertThat(client.categoryDescription?.text).isEmpty()
    }
}
