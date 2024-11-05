package com.skedgo.geocoding;


import com.skedgo.geocoding.agregator.GCAppResultInterface;
import com.skedgo.geocoding.agregator.GCFoursquareResultInterface;
import com.skedgo.geocoding.agregator.GCGoogleResultInterface;
import com.skedgo.geocoding.agregator.GCResultInterface;
import com.skedgo.geocoding.agregator.GCSkedGoResultInterface;
import com.skedgo.geocoding.agregator.MGAResultInterface;

import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * scored single result - without duplicates
 */
public class ScoringResult<T extends GCResultInterface> implements MGAResultInterface<T> {

    private T providerResult;
    private int score;

    //-1 if the source not calculate the score
    private int distanceScore = -1;
    private int nameScore = -1;
    private int addressScore = -1;
    private int popularityScore = -1;

    public ScoringResult(T providerResult) {
        this.providerResult = providerResult;
    }

    @NotNull
    @Override
    public T getResult() {
        return providerResult;
    }

    @Override
    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    @Override
    public List<MGAResultInterface<T>> getDuplicates() {
        return null;
    }

    @Override
    public MGAResultInterface<T> getClassRepresentative() {
        return null;
    }

    @Override
    public int getNameScore() {
        return nameScore;
    }

    public void setNameScore(int nameScore) {
        this.nameScore = nameScore;
    }

    @Override
    public int getAddressScore() {
        return addressScore;
    }

    public void setAddressScore(int addressScore) {
        this.addressScore = addressScore;
    }

    @Override
    public int getDistanceScore() {
        return distanceScore;
    }

    public void setDistanceScore(int distanceScore) {
        this.distanceScore = distanceScore;
    }

    @Override
    public int getPopularityScore() {
        return popularityScore;
    }

    public void setPopularityScore(int popularityScore) {
        this.popularityScore = popularityScore;
    }

    public boolean equals(MGAResultInterface<T> element) {
        return isDuplicate(this, element);
    }

    private boolean isDuplicate(MGAResultInterface<T> mgaResult, MGAResultInterface<T> mgaResult1) {
        T result = mgaResult.getResult();
        T result1 = mgaResult1.getResult();

        if (isRegion(result) || isRegion(result1))
            return false;
        else {
            String mgaResultName = mgaResult.getResult().getName();
            String mgaResult1Name = mgaResult1.getResult().getName();
            LatLng mgaResultLL = new LatLng(mgaResult.getResult().getLat(), mgaResult.getResult().getLng());
            LatLng mgaResult1LL = new LatLng(mgaResult1.getResult().getLat(), mgaResult.getResult().getLng());

            return (mgaResultName.contains(mgaResult1Name) || mgaResult1Name.contains(mgaResultName)) && (mgaResultLL.distanceInMetres(mgaResult1LL) < 10);
        }
    }


    private boolean isRegion(T result) {
        if (result instanceof GCAppResultInterface) {
            GCAppResultInterface apiResultCandidate = (GCAppResultInterface) result;
            return apiResultCandidate.getAppResultSource().equals(GCAppResultInterface.Source.Regions);
        } else
            return false;
    }

    private boolean isBHresult(T result) {
        return (result instanceof GCAppResultInterface || result instanceof GCSkedGoResultInterface);
    }

    private boolean isFromDifferentSource(T result1, T result2) {
        if ((result1 instanceof GCAppResultInterface && result2 instanceof GCAppResultInterface) ||
            (result1 instanceof GCSkedGoResultInterface && result2 instanceof GCSkedGoResultInterface) ||
            (result1 instanceof GCGoogleResultInterface && result2 instanceof GCGoogleResultInterface) ||
            (result1 instanceof GCFoursquareResultInterface && result2 instanceof GCFoursquareResultInterface))
            return false;
        else
            return true;
    }


}
