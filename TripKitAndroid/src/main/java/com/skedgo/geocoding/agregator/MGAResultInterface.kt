package com.skedgo.geocoding.agregator;

import java.io.Serializable;
import java.util.List;

/**
 * this class represents a scored result, it could be single or have duplicates
 */
public interface MGAResultInterface<T extends GCResultInterface> extends Serializable {

    //  returns query result
    T getResult();

    //  total score for the result
    int getScore();

    //  returns all duplicates for the result
    List<MGAResultInterface<T>> getDuplicates();

    //  returns the scored result which is the class representative for the duplicate list
    MGAResultInterface<T> getClassRepresentative();

    //next was added to test
//    name score value
    int getNameScore();

    //    address score value
    int getAddressScore();

    //    distance score value
    int getDistanceScore();

    //    populariry score value
    int getPopularityScore();
}
