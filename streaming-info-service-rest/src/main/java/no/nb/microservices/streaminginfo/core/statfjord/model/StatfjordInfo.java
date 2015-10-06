package no.nb.microservices.streaminginfo.core.statfjord.model;

/**
 * Created by andreasb on 05.10.15.
 */
public class StatfjordInfo {

    private String urn;
    private int offset;
    private int extent;

    public StatfjordInfo(String urn, int offset, int extent) {
        this.urn = urn;
        this.offset = offset;
        this.extent = extent;
    }

    public String getUrn() {
        return urn;
    }

    public void setUrn(String urn) {
        this.urn = urn;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public int getExtent() {
        return extent;
    }

    public void setExtent(int extent) {
        this.extent = extent;
    }
}
