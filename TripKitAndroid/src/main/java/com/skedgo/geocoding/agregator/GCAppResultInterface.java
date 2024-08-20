package com.skedgo.geocoding.agregator;

/**
 * Information that the user saves in the app
 */
public interface GCAppResultInterface extends GCResultInterface {

    //  address value
    String getSubtitle();

    //  source that provides the result
    Source getAppResultSource();

    //    is true if the result was set as favourite by the user false otherwise.
    boolean isFavourite();

    //   source that provides the result
    enum Source {
        AddressBook,
        Regions,
        Calendar,
        History
    }

}
