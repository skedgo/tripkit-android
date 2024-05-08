package com.skedgo.geocoding.agregator;

import com.skedgo.geocoding.*;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * Scoring formulas
 * Favourites: max(title, address)
 * Search history: max(title, address)
 * Regions: distance
 * Address book: (title + address) / 2
 * Calendar: (title + address) / 2
 * SkedGo transit stops: popularity   -> ((min(popularity, GOOD_SCORE)) / (GOOD_SCORE / 100)) * 2
 * SkedGo others: title
 * Google: (max(title, address) * 3 + distance) / 4
 * Google Autocomplete: (title * 3) / 4
 * Foursquare: ((title * 3 + distance) / 4) * suburb
 * Title score: score based on input string against the title of the result
 * Address score: score based on input string against address of the result
 * Distance score: score based on distance to center or provided region
 * Suburb score: bonus score if result is a suburb
 * Popularity score: score based on popularity of result as determined by server
 */

public class MultiSourceGeocodingAggregator<T extends GCResultInterface> {

    private static MultiSourceGeocodingAggregator instance  = null;

    public static MultiSourceGeocodingAggregator getInstance() {
        if (instance == null)
            instance = new MultiSourceGeocodingAggregator<>();
        return instance;
    }

    private MultiSourceGeocodingAggregator(){}

    public List<MGAResultInterface<T>> aggregate(GCQueryInterface userQuery, List<List<T>>  providersResults){
        GCQuery query;
        List<MGAResultInterface<T>> scoredResults = new ArrayList<>();
        if (userQuery instanceof GCQuery){
            query = (GCQuery) userQuery;
        }
        else{
            if (userQuery.getBounds() instanceof GCBoundingBox){
                query = new GCQuery(userQuery.getQueryText(), (GCBoundingBox) userQuery.getBounds());
            }else{
                query = new GCQuery(userQuery.getQueryText(), new GCBoundingBox(userQuery.getBounds()));
            }
        }

        for (List<T> providerResults : providersResults) {
            if (providerResults != null){
                for (T candidate : providerResults) {
                    ScoringResult<T> scoringResult = calculateScore(query,candidate);
                    if (scoringResult != null && scoringResult.getScore() != 0) {
                        scoredResults.add(scoringResult);
                    }
                }
            }
        }

        scoredResults = GeocodeUtilities.sortByScore(scoredResults);
        return scoredResults;
    }

    public List<GCResultInterface> flattenAggregate(GCQueryInterface userQuery, List<List<T>> providersResults){
        List<MGAResultInterface<T>> aggregates =  aggregate(userQuery, providersResults);
        List<GCResultInterface> results = new ArrayList<>();
        for (MGAResultInterface<T> result : aggregates){
            results.add(result.getResult());
        }
        return results;
    }


    private ScoringResult<T> calculateScore(GCQuery query, T candidate){

        if (candidate instanceof GCGoogleResultInterface)
            return selectGoogleScore(query, (GCGoogleResultInterface) candidate);
        if (candidate instanceof GCFoursquareResultInterface)
            return calculateFoursquareScoring(query, (GCFoursquareResultInterface) candidate);
        if (candidate instanceof GCSkedGoResultInterface)
            return calculateSkedGoScoring(query, (GCSkedGoResultInterface) candidate);
        if (candidate instanceof GCAppResultInterface){
            GCAppResultInterface apiResultCandidate = (GCAppResultInterface) candidate;
            switch (apiResultCandidate.getAppResultSource()) {
                case History:
                    return calculateHistoryScoring(query, apiResultCandidate);
                case Calendar:
                    return calculateCalendarScoring(query, apiResultCandidate);
                case AddressBook:
                    return calculateAddressBookScoring(query, apiResultCandidate);
                case Regions:
                    return calculateRegionsScoring(query, apiResultCandidate);
            }
        }
        return null;
    }

    private ScoringResult<T> selectGoogleScore(GCQuery query, GCGoogleResultInterface candidate){
        if(candidate.getLat() != null && candidate.getLng() != null && candidate.getAddress() != null){
            return calculateGoogleScoring(query, candidate);
        }else{
            return calculateAutocompleteScore(query, candidate);
        }
    }


    private ScoringResult<T> calculateGoogleScoring(GCQuery query, GCGoogleResultInterface candidate){
        ScoringResult<T> scoringResult = new ScoringResult(candidate);
        int nameScore = GeocodeUtilities.scoreBasedOnNameMatchBetweenSearchTerm(query.getQueryText(), candidate.getName());
        int addressScore = GeocodeUtilities.scoreBasedOnNameMatchBetweenSearchTerm(query.getQueryText(), candidate.getAddress());
        int stringScore = Math.max(nameScore, addressScore);
        scoringResult.setNameScore(nameScore);
        scoringResult.setAddressScore(addressScore);

        int distanceScore = 0;
        if (candidate.getLat() != -1 && candidate.getLng() != -1) {
            LatLng coordinate = new LatLng(candidate.getLat(), candidate.getLng());
            distanceScore = GeocodeUtilities.scoreBasedOnDistanceFromCoordinate(coordinate, query.getBounds(), query.getBounds().center(), false);
            scoringResult.setDistanceScore(distanceScore);
        }
        int rawScore = (stringScore * 3 + distanceScore) / 4;
        int min = 15;
        int max = 75;
        int totalScore = GeocodeUtilities.rangedScoreForScore(rawScore, min, max);
        scoringResult.setScore(totalScore);
        return scoringResult;
    }


    private ScoringResult<T> calculateAutocompleteScore(GCQuery query, GCGoogleResultInterface candidate) {
        ScoringResult<T> scoringResult = new ScoringResult(candidate);
        int nameScore = GeocodeUtilities.scoreBasedOnNameMatchBetweenSearchTerm(query.getQueryText(), candidate.getName());
        int rawScore = (nameScore * 3) /4;
        int min = 15;
        int max = 75;
        int totalScore = GeocodeUtilities.rangedScoreForScore(rawScore, min, max);
        scoringResult.setScore(totalScore);
        return scoringResult;
    }

    private ScoringResult<T> calculateFoursquareScoring(GCQuery query, GCFoursquareResultInterface candidate){
        ScoringResult<T> scoringResult = new ScoringResult(candidate);

        //discard uncategorized and not verified foursquare results
        if (!candidate.isVerified() && (candidate.getCategories() == null || candidate.getCategories().isEmpty()))
            return null;

        int titleScore = GeocodeUtilities.scoreBasedOnNameMatchBetweenSearchTerm(query.getQueryText(), candidate.getName());

        if (titleScore == 0) {
            scoringResult.setScore(0);
            scoringResult.setNameScore(0);
            return  scoringResult;
        }

        LatLng coordinate = new LatLng(candidate.getLat(), candidate.getLng());
        int distanceScore = GeocodeUtilities.scoreBasedOnDistanceFromCoordinate(coordinate, query.getBounds(), query.getBounds().center(), false);

        int rawScore = (titleScore * 3 + distanceScore) / 4;
        if (GeocodeUtilities.isSuburb(candidate)){
            rawScore *= 2;
        }
        scoringResult.setNameScore(titleScore);
        scoringResult.setDistanceScore(distanceScore);

        // Even verified results can be not great matches, so we keep the maximum fixed, but
        // raise the lower end. Overall, we want foursquare to have a lower maximum than
        // Apple as Apple tends to provide better exact matches.
        int minimum, maximum;
        if (candidate.isVerified()) {
            minimum = 33;
            maximum = 66;
        } else {
            minimum = 15;
            maximum = 66;
        }

        int totalScore = GeocodeUtilities.rangedScoreForScore(rawScore, minimum, maximum);
        scoringResult.setScore(totalScore);

        return scoringResult;
    }

    private ScoringResult<T> calculateSkedGoScoring(GCQuery query, GCSkedGoResultInterface candidate) {
        ScoringResult<T> scoringResult = new ScoringResult(candidate);
        if (candidate.getResultClass().equalsIgnoreCase("StopLocation")) {
            int GOOD_SCORE = 1000;
            int popularity = candidate.getPopularity();
//            int popularityScore = (Math.min(popularity, GOOD_SCORE)) / (GOOD_SCORE / 100)
            int popularityScore = ((Math.min(popularity, GOOD_SCORE)) / (GOOD_SCORE / 100)) * 2;

            popularityScore = (candidate.getModeIdentifiers() != null && !candidate.getModeIdentifiers().isEmpty()) ?
                    GeocodeUtilities.rangedScoreForScore(popularityScore, 50, 90) :
                    GeocodeUtilities.rangedScoreForScore(popularityScore, 30, 80);

            if (popularity > GOOD_SCORE) {
                int moreThanGood = popularityScore / GOOD_SCORE;
                int bonus = GeocodeUtilities.rangedScoreForScore(moreThanGood, 0, 10);
                popularityScore += bonus;
            }

            scoringResult.setPopularityScore(candidate.getPopularity());
            scoringResult.setScore(popularityScore);
            return scoringResult;
        }else{
            if (!query.getQueryText().isEmpty()){
                int nameScore = GeocodeUtilities.scoreBasedOnNameMatchBetweenSearchTerm(query.getQueryText(), candidate.getName());

                int totalScore = (candidate.getModeIdentifiers() != null && !candidate.getModeIdentifiers().isEmpty()) ?
                        GeocodeUtilities.rangedScoreForScore(nameScore,50,90):
                        GeocodeUtilities.rangedScoreForScore(nameScore,0,50);
                scoringResult.setScore(totalScore);
                scoringResult.setNameScore(nameScore);
                return scoringResult;
            }else{
                int totalScore = GeocodeUtilities.rangedScoreForScore(candidate.getPopularity(),0,50);
                scoringResult.setPopularityScore(candidate.getPopularity());
                scoringResult.setScore(totalScore);
                return scoringResult;
            }
        }
    }

    //favourites - searchHistory
    private ScoringResult<T> calculateHistoryScoring(GCQuery query, GCAppResultInterface candidate) {
        ScoringResult<T> scoringResult = new ScoringResult(candidate);

        int rawScore;
        boolean matchAgainstString = query.getQueryText().length() > 0;

        if (matchAgainstString) {
            int nameScore = GeocodeUtilities.scoreBasedOnNameMatchBetweenSearchTerm(query.getQueryText(), candidate.getName());
            int addressScore =  GeocodeUtilities.scoreBasedOnNameMatchBetweenSearchTerm(query.getQueryText(), candidate.getSubtitle());
            scoringResult.setNameScore(nameScore);
            scoringResult.setAddressScore(addressScore);
            rawScore = Math.max(nameScore, addressScore);
        } else {
            rawScore = 100;
            scoringResult.setNameScore(rawScore);
        }


        int min = candidate.isFavourite() ? 90  : 50;
        int max = candidate.isFavourite() ? 100 : 90;
        int totalScore  = GeocodeUtilities.rangedScoreForScore(rawScore, min, max);
        scoringResult.setScore(totalScore);

        return scoringResult;
    }

    private ScoringResult<T> calculateCalendarScoring(GCQueryInterface query, GCAppResultInterface candidate) {
        ScoringResult<T> scoringResult = new ScoringResult(candidate);
        String searchTerm = query.getQueryText();
        int nameScore =  GeocodeUtilities.scoreBasedOnNameMatchBetweenSearchTerm(searchTerm, candidate.getName());
        int locationScore = GeocodeUtilities.scoreBasedOnNameMatchBetweenSearchTerm(searchTerm, candidate.getSubtitle());
        scoringResult.setNameScore(nameScore);
        scoringResult.setAddressScore(locationScore);

        int rawScore = Math.min(100, (nameScore + locationScore) / 2);

        int min = 50;
        int max = 90;
        int totalScore =  GeocodeUtilities.rangedScoreForScore(rawScore, min, max);
        scoringResult.setScore(totalScore);

        return scoringResult;
    }

    // address book & calendar use the same algorithm to calculate scoring
    private ScoringResult<T> calculateAddressBookScoring(GCQueryInterface query, GCAppResultInterface candidate) {
        return calculateCalendarScoring(query, candidate);
    }

    private ScoringResult<T> calculateRegionsScoring(GCQuery query, GCAppResultInterface candidate) {
        ScoringResult<T> scoringResult = new ScoringResult(candidate);
        LatLng coordinate = new LatLng(candidate.getLat(), candidate.getLng());
        int rawScore = GeocodeUtilities.scoreBasedOnDistanceFromCoordinate(coordinate, query.getBounds(), query.getBounds().center(), false);
        scoringResult.setDistanceScore(rawScore);

        int min = 50;
        int max = 90;
        int totalScore =  GeocodeUtilities.rangedScoreForScore(rawScore, min, max);
        scoringResult.setScore(totalScore);

        return scoringResult;
    }
}
