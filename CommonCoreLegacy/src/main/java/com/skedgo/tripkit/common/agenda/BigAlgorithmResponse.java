package com.skedgo.tripkit.common.agenda;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * @see <a href="https://redmine.buzzhives.com/projects/buzzhives/wiki/Big_Algorithm_API">Big Algorithm API</a>
 */
public class BigAlgorithmResponse {
  @SerializedName("result") private BigAlgorithmResult result;
  private ArrayList<TrackItem> trackItems;

  public BigAlgorithmResult result() {
    return result;
  }

  public ArrayList<TrackItem> getTrackItems() {
    return trackItems;
  }

  public void setTrackItems(ArrayList<TrackItem> trackItems) {
    this.trackItems = trackItems;
  }
}