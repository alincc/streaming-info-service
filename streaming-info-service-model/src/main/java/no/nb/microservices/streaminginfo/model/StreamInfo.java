package no.nb.microservices.streaminginfo.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by andreasb on 29.09.15.
 */
public class StreamInfo {
    private String urn;
    private double playDuration;
    private double playStart;
    private List<StreamQuality> qualities = new ArrayList<>();

    public StreamInfo(String urn) {
        this.urn = urn;
    }

    public StreamInfo(String name, double playDuration, double playStart) {
        this.urn = name;
        this.playDuration = playDuration;
        this.playStart = playStart;
    }

    public StreamInfo(String urn, double playDuration, double playStart, List<StreamQuality> qualities) {
        this.urn = urn;
        this.playDuration = playDuration;
        this.playStart = playStart;
        this.qualities = qualities;
    }

    public String getUrn() {
        return urn;
    }

    public void setUrn(String urn) {
        this.urn = urn;
    }

    public double getPlayDuration() {
        return playDuration;
    }

    public void setPlayDuration(double playDuration) {
        this.playDuration = playDuration;
    }

    public double getPlayStart() {
        return playStart;
    }

    public void setPlayStart(double playStart) {
        this.playStart = playStart;
    }

    public List<StreamQuality> getQualities() {
        return qualities;
    }

    public void setQualities(List<StreamQuality> qualities) {
        this.qualities = qualities;
    }
}
