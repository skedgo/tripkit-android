package com.skedgo.tripkit.booking

import com.google.gson.Gson
import com.skedgo.tripkit.booking.quickbooking.Fare
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class FareQuantityJsonTest {
    @Test
    fun `fare parsing preserves supplied minimum without selecting optional fare`() {
        val gson = Gson()
        val fare = gson.fromJson("""{"id":"companion","name":"Additional passenger","price":100,"currency":"USD","min":2,"max":5}""", Fare::class.java)
        assertThat(gson.toJsonTree(fare).asJsonObject.get("min")?.asInt).isEqualTo(2)
        assertThat(fare.value ?: 0).isEqualTo(0L)
        assertThat(fare.max).isEqualTo(5)
    }
    @Test
    fun `one to three and two to five selected quantities obey both bounds`() {
        listOf(1 to 3, 2 to 5).forEach { (min, max) ->
            val fare = fare(""""min":$min,"max":$max""")
            assertThat(fare.increasedQuantity(0)).isEqualTo(min.toLong())
            assertThat(fare.increasedQuantity(max.toLong())).isEqualTo(max.toLong())
            assertThat(fare.canIncreaseQuantity(max.toLong())).isFalse()
            assertThat(fare.decreasedQuantity(min.toLong())).isEqualTo(0L)
            assertThat(fare.decreasedQuantity(max.toLong())).isEqualTo(max - 1L)
            assertThat(fare.isValidQuantity(min - 1L)).isEqualTo(min == 1)
            assertThat(fare.isValidQuantity(max + 1L)).isFalse()
            assertThat(fare.isValidQuantity(0)).isTrue()
        }
    }

    @Test
    fun `missing bounds retain optional selection without invented maximum`() {
        listOf("", "\"min\":2", "\"max\":3").forEach { fields ->
            val fare = fare(fields)
            assertThat(fare.isValidQuantity(0)).isTrue()
            assertThat(fare.increasedQuantity(0)).isEqualTo(if (fields.contains("min")) 2L else 1L)
            assertThat(fare.isValidQuantity(-1)).isFalse()
        }
        assertThat(fare("\"min\":2").isValidQuantity(100_000)).isTrue()
        assertThat(fare("").increasedQuantity(Long.MAX_VALUE)).isEqualTo(Long.MAX_VALUE)
        assertThat(fare("").canIncreaseQuantity(Long.MAX_VALUE)).isFalse()
    }

    @Test
    fun `invalid bounds cannot select a fare but do not force selection`() {
        listOf("\"min\":-1", "\"max\":0", "\"min\":5,\"max\":2").forEach { fields ->
            val fare = fare(fields)
            assertThat(fare.canIncreaseQuantity(0)).isFalse()
            assertThat(fare.increasedQuantity(0)).isEqualTo(0L)
            assertThat(fare.isValidQuantity(1)).isFalse()
            assertThat(fare.isValidQuantity(0)).isTrue()
        }
    }

    @Test
    fun `fractional fare quantities and bounds are rejected by JSON parsing`() {
        listOf("value", "min", "max").forEach { field ->
            org.assertj.core.api.Assertions.assertThatThrownBy { fare("\"$field\":1.5") }
                .isInstanceOf(com.google.gson.JsonSyntaxException::class.java)
        }
    }

    private fun fare(fields: String): Fare = Gson().fromJson(
        """{"id":"companion"${if (fields.isEmpty()) "" else ",$fields"}}""", Fare::class.java
    )
}
