package skedgo.tripkit.analytics

fun UserInfo.convertToMap(): Map<String, Any> =
    mapOf<String, Any>(
        "source" to this.source as Any,
        "choiceSet" to this.choiceSet as Any
    )
