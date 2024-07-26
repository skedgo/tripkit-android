package com.skedgo.tripkit.a2brouting

/**
 * Weight values are stored in app as a value in range [0 - 100].
 * The server expects weights between 0.1 and 2.0. So we convert them.
 * @receiver Value must be in range of 0 to 100.
 * @return Corresponding value between 0.1 - 2.0.
 */
fun Int.toProfileWeight(): Float {
    require(this in 0..100) {
        "Value must be in range of 0 to 100. Found: $this."
    }
    return when (this) {
        0 -> 0.1f
        else -> this / 50f
    }
}