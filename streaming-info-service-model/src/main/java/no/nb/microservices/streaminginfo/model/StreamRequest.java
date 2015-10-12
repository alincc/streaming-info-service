package no.nb.microservices.streaminginfo.model;

/**
 * Created by andreasb on 29.09.15.
 */
public class StreamRequest {
    private String urn;
    private String site;

    public StreamRequest() {
    }

    public StreamRequest(String urn) {
        this.urn = urn;
    }

    public StreamRequest(String urn, String site) {
        this.urn = urn;
        this.site = site;
    }

    public String getUrn() {
        return urn;
    }

    public void setUrn(String urn) {
        this.urn = urn;
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }
}
