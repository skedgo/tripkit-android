package com.skedgo.geocoding.agregator

/**
 * Information that the user saves in the app
 */
interface GCAppResultInterface : GCResultInterface {

    // Address value
    val subtitle: String

    // Source that provides the result
    val appResultSource: Source

    // True if the result was set as favorite by the user, false otherwise
    val isFavourite: Boolean

    // Source that provides the result
    enum class Source {
        AddressBook,
        Regions,
        Calendar,
        History
    }
}
