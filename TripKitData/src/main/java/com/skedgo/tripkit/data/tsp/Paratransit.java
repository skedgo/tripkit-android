package com.skedgo.tripkit.data.tsp;

import com.google.gson.annotations.SerializedName;

import androidx.annotation.Nullable;

public class Paratransit {
    @SerializedName("URL")
    private String url;
    @SerializedName("name")
    private String name;
    @SerializedName("number")
    private String number;

    public Paratransit() {
    }

    public Paratransit(String url, String name, String number) {
        this.url = url;
        this.name = name;
        this.number = number;
    }

    @Nullable
    public String url() {
        return url;
    }

    @Nullable
    public String name() {
        return name;
    }

    @Nullable
    public String number() {
        return number;
    }
}