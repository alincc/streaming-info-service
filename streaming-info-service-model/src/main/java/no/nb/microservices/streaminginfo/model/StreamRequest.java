package no.nb.microservices.streaminginfo.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by andreasb on 29.09.15.
 */
public class StreamRequest {
    private String urn;
    private String ip;
    private String ssoToken;
    private int offset;
    private int extent;
    private String query;

    @JsonCreator
    public StreamRequest(@JsonProperty("urn") String urn,
                         @JsonProperty("ip") String ip,
                         @JsonProperty("query") String query) {
        this.urn = urn;
        this.ip = ip;
        this.query = query;
        parseQuery(query);
    }

    private void parseQuery(String query) {
        Map<String, String> queryMap = getQueryMap(query);
        this.ssoToken = queryMap.get("ssoToken");
        this.offset = Integer.parseInt(queryMap.get("offset"));
        this.extent = Integer.parseInt(queryMap.get("extent"));
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

    public void setQuery(String query) {
        parseQuery(query);
        this.query = query;
    }

    private Map<String, String> getQueryMap(String query) {
        String[] params = query.split("&");
        Map<String, String> map = new HashMap<>();
        for (String param : params) {
            String[] keyValue = param.split("=");
            if (keyValue.length == 2) {
                map.put(keyValue[0], keyValue[1]);
            }
        }
        return map;
    }
}
