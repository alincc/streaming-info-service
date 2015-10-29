package no.nb.microservices.streaminginfo.core.resource.model;

import java.io.Serializable;

/**
 * Created by andreasb on 29.10.15.
 */
public class MediaResourceId implements Serializable {

    String identifier;

    int componentNo;

    public MediaResourceId() {
    }

    public MediaResourceId(String identifier, int componentNo) {
        this.identifier = identifier;
        this.componentNo = componentNo;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public int getComponentNo() {
        return componentNo;
    }

    public void setComponentNo(int componentNo) {
        this.componentNo = componentNo;
    }
}