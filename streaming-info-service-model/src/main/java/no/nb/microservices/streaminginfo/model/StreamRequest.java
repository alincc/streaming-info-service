package no.nb.microservices.streaminginfo.model;

/**
 * Created by andreasb on 29.09.15.
 */
public class StreamRequest {
    private String urn;
    private String ip;
    private String ssoToken;
    private int offset;
    private int extent;

    public StreamRequest() {}

    public StreamRequest(String urn, String ip, String ssoToken) {
        this.urn = urn;
        this.ip = ip;
        this.ssoToken = ssoToken;
    }

    public StreamRequest(String urn, String ip, String ssoToken, int offset, int extent) {
        this.urn = urn;
        this.ip = ip;
        this.ssoToken = ssoToken;
        this.offset = offset;
        this.extent = extent;
    }

    public String getUrn() {
        return urn;
    }

    public void setUrn(String urn) {
        this.urn = urn;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getSsoToken() {
        return ssoToken;
    }

    public void setSsoToken(String ssoToken) {
        this.ssoToken = ssoToken;
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
