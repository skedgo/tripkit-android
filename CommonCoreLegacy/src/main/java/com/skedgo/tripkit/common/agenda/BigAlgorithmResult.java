package com.skedgo.tripkit.common.agenda;

import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;
import com.skedgo.tripkit.common.model.RealtimeAlert;

import java.util.ArrayList;
import java.util.List;

/**
 * @see <a href="https://redmine.buzzhives.com/projects/buzzhives/wiki/Big_Algorithm_API">Big Algorithm API</a>
 */
public class BigAlgorithmResult {
  @SerializedName("track") private ArrayList<JsonObject> track;
  @SerializedName("segmentTemplates") private ArrayList<JsonObject> segmentTemplates;
  @SerializedName("alerts") private List<RealtimeAlert> alerts;

  public ArrayList<JsonObject> track() {
    return track;
  }

  public ArrayList<JsonObject> segmentTemplates() {
    return segmentTemplates;
  }

  public List<RealtimeAlert> alerts() {
    return alerts;
  }
}