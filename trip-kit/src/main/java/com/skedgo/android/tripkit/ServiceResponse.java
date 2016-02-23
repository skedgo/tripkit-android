package com.skedgo.android.tripkit;

import android.support.annotation.Nullable;

import com.google.gson.annotations.SerializedName;
import com.skedgo.android.common.model.Shape;

import java.util.List;

public class ServiceResponse {
  @SerializedName("realTimeStatus")
  private String realTimeStatus;

  @SerializedName("type")
  private String type;

  @SerializedName("shapes")
  private List<Shape> shapes;

  public ServiceResponse(String realTimeStatus,
                         String type,
                         List<Shape> shapes) {
    this.realTimeStatus = realTimeStatus;
    this.type = type;
    this.shapes = shapes;
  }

  public ServiceResponse() {}

  @Nullable
  public String getRealTimeStatus() {
    return realTimeStatus;
  }

  @Nullable
  public String getType() {
    return type;
  }

  @Nullable
  public List<Shape> getShapes() {
    return shapes;
  }
}