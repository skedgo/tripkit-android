package com.skedgo.geocoding;

import com.skedgo.geocoding.agregator.*;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;


public class GeocodeUtilities {

    public static int scoreBasedOnNameMatchBetweenSearchTerm(String searchTerm, String candidate) {
        searchTerm = stringForScoringOfString(searchTerm);
        candidate = stringForScoringOfString(candidate);

        return scoreBetweenSearchTerm(searchTerm, candidate);
    }

    public static String stringForScoringOfString(String term){
        String updatedTerm = "";
        if (term != null)
            updatedTerm = term.trim();

        updatedTerm = updatedTerm.replaceAll("\\s+", " ");
        String result = "";
        for(Character character : updatedTerm.toCharArray()){
            if (isAlphanumeric(character))
                result += character.toString().toLowerCase();
        }
        return result;
    }

    public static int scoreBetweenSearchTerm(String target, String candidate){

        if (target.length() == 0)
            return candidate.length() == 0 ? 100 : 0;

        if (candidate.length() == 0)
            return 100; // having typed yet means a perfect match of everything you've typed so far

        if (target.equalsIgnoreCase(candidate)) {
            return 100;
        }

        if (isAbbreviationFor(target, candidate))
            return 95;

        if (isAbbreviationFor(candidate, target) ) {
            return 90;
        }

        int excess = candidate.length() - target.length();
        int fullMatchRangeLocation = candidate.indexOf(target);
        if (fullMatchRangeLocation == 0) {
            // matches right at start
            return calculateScoring(100, excess, 75);
        } else if (fullMatchRangeLocation != -1) {
            String before = candidate.substring(fullMatchRangeLocation -1, fullMatchRangeLocation);
            if (before.matches("\\S")) {
                // matches beginning of word
                return calculateScoring(75, fullMatchRangeLocation * 2 + excess, 33);
            } else {
                // in-word match
                return calculateScoring(40, excess, 15);
            }
        }

//         non-substring matches
        String[] targetWords = target.split(" ");
        int lastIndex = 0;
        for (String targetWord : targetWords) {
            int location =  candidate.indexOf(targetWord);
            if (location == -1) {
                return 0; // missing a word!
            } else if(location >= lastIndex){
                // still in order, keep going
               lastIndex = location;
            }  else {
                // wrong order, abort with penalty
                return calculateScoring(10, excess, 0);
            }
        }

        // contains all target words in order
        // do we have all the finished words
        for (int i = 0; i < targetWords.length -1; i++ ){
            String targetWord = targetWords[i];
            int after = candidate.indexOf(targetWord) + targetWord.length() + 1;
            if(String.valueOf(candidate.charAt(after)).matches("\\S")){
                // full word match, continue with next
            }else{
                //  candidate doesn't have a completed word
                return calculateScoring(33, excess,10);
            }
        }

        return calculateScoring(66,excess,40);
    }

//    It resolves abbreviations such as ("MOMA", "museum of modern art")
    public static boolean isAbbreviationFor(String abbreviation, String text){

        abbreviation = abbreviation.toLowerCase();
        text = text.toLowerCase();
        if (abbreviation.length() <= 2) {
            return false;
        }

        String letter = abbreviation.substring(0,1);
        if (!text.startsWith(letter))
            return false;

        String [] parts = text.split(" ");
        if (parts.length != abbreviation.length())
            return false;

        for (int i=1; i<abbreviation.length(); i++){
            letter = abbreviation.substring(i,i+1);
            String word = parts[i];
            if (! word.startsWith(letter))
                return false;
        }

        return true;
    }

    private static  int calculateScoring(int maximum, int penalty, int minimum){
        if (penalty > maximum - minimum) {
            return minimum;
        } else {
            return maximum - penalty;
        }
    }

    public static int rangedScoreForScore(int score, int minimum, int maximum){
        if (score > 100)
            score = 100;
        int range = maximum - minimum;
        float percentage = score / 100.f;
        return (int) Math.ceil(percentage * range) + minimum;
    }

    public static int scoreBasedOnDistanceFromCoordinate(LatLng coordinate, GCBoundingBox region, LatLng regionCenter, boolean longDistance){

//        That's covering the special case of passing in the whole world. In that case everything scores 100%.
        GCBoundingBox worldRegion = GCBoundingBox.World;
        if (region != null && (Math.abs(worldRegion.getLatitudeDelta() - region.getLatitudeDelta()) < 1 &&
                Math.abs(worldRegion.getLongitudeDelta() - region.getLongitudeDelta()) < 1))
            return 100;

        double meters = coordinate.distanceInMetres(regionCenter);
        double zeroScoreDistance = longDistance ? 20000000 : 25000;
        if (meters >= zeroScoreDistance) {
            return 0;
        }

        double match = longDistance ? Math.sqrt(meters) / Math.sqrt(zeroScoreDistance) : meters / zeroScoreDistance;
        double proportion = 1.0 - match;
        int max = 100;
        int score = (int) (proportion * max);
        return score;
    }


    private static boolean isAlphanumeric(char character){
        return (Character.isLetter(character) || Character.isDigit(character) || String.valueOf(character).matches("\\S"));
    }

    public static boolean isSuburb(GCFoursquareResultInterface foursquareResult){
        boolean isSuburb = false;

        for (String categoryName : foursquareResult.getCategories()) {
            if (categoryName.equalsIgnoreCase("States & Municipalities")) {
                isSuburb = true;
                break;
            }
        }
        return isSuburb;
    }

    public static  <T extends GCResultInterface>  List<MGAResultInterface<T>> sortByImportance(List<MGAResultInterface<T>> scoreResults) {
        Collections.sort(scoreResults, new Comparator<MGAResultInterface>() {
            @Override
            public int compare(MGAResultInterface o1, MGAResultInterface o2) {
                return compareInt(getRanking2Group(o2), getRanking2Group(o1));
            }
        });

        return scoreResults;
    }

    public static <T extends GCResultInterface> List<MGAResultInterface<T>> sortByScore(List<MGAResultInterface<T>> scoreResults) {
        Collections.sort(scoreResults, new Comparator<MGAResultInterface>() {
            @Override
            public int compare(MGAResultInterface o1, MGAResultInterface o2) {
                return compareInt(o2.getScore(), o1.getScore());
            }
        });
        return scoreResults;
    }

    public static int compareInt(int x, int y) {
        return (x < y) ? -1 : ((x == y) ? 0 : 1);
    }

    private static int getRanking2Group(MGAResultInterface element){
        if (element.getResult() instanceof GCSkedGoResultInterface)
            return 10;

        if (element.getResult() instanceof GCAppResultInterface){
            GCAppResultInterface appResult = (GCAppResultInterface) element.getResult();
            if (appResult.isFavourite())
                return 11;
            else
                return 9;
        }

        if (element.getResult() instanceof GCFoursquareResultInterface){
            GCFoursquareResultInterface foursquareResult = (GCFoursquareResultInterface) element.getResult();
            if (foursquareResult.isVerified())
                return 8;
            else
                return 6;
        }

        return 7;
    }



}
