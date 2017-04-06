package skedgo.tripkit.analytics

internal fun UserInfo.toMutableMap(): MutableMap<String, Any> =
    mutableMapOf<String, Any>(
        "source" to source.value,
        "choiceSet" to this.choiceSet as Any
    )
