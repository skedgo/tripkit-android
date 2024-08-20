package com.skedgo.geocoding;

import com.skedgo.geocoding.agregator.GCResultInterface;
import com.skedgo.geocoding.agregator.MGAResultInterface;

import java.util.ArrayList;
import java.util.List;

/**
 * scored result with duplicates
 */
public class GroupScoringResult<T extends GCResultInterface> implements MGAResultInterface<T> {

    private List<MGAResultInterface<T>> duplicates = null;

    public GroupScoringResult() {
    }

    @Override
    public T getResult() {
        return duplicates.get(0).getResult();
    }

    @Override
    public int getScore() {
        return duplicates.get(0).getScore();
    }

    @Override
    public List<MGAResultInterface<T>> getDuplicates() {
        return duplicates;
    }

    @Override
    public MGAResultInterface<T> getClassRepresentative() {
        return duplicates.get(0);
    }

    @Override
    public int getNameScore() {
        return duplicates.get(0).getNameScore();
    }

    @Override
    public int getAddressScore() {
        return duplicates.get(0).getAddressScore();
    }

    @Override
    public int getDistanceScore() {
        return duplicates.get(0).getDistanceScore();
    }

    @Override
    public int getPopularityScore() {
        return duplicates.get(0).getPopularityScore();
    }


    public void addDuplicate(ScoringResult<T> scoringResult) {
        if (duplicates == null)
            duplicates = new ArrayList<>();
        duplicates.add(scoringResult);
        duplicates = GeocodeUtilities.sortByImportance(duplicates);
    }

    public void addDuplicates(List<MGAResultInterface<T>> scoringResults) {
        if (duplicates == null)
            duplicates = new ArrayList<>();
        duplicates.addAll(scoringResults);
        duplicates = GeocodeUtilities.sortByImportance(duplicates);
    }

    public ScoringResult<T> getScoringResult() {
        return (ScoringResult<T>) duplicates.get(0);
    }
}
