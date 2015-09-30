package no.nb.microservices.streaminginfo.core.resource.model;

/**
 * Created by andreasb on 29.09.15.
 */
public class MediaResource {
    private String identifier;
    private int componentNr;
    private String imageFile;
    private int size;
    private String purpose;

    public MediaResource(String identifier, int componentNr, String imageFile, int size, String purpose) {
        this.identifier = identifier;
        this.componentNr = componentNr;
        this.imageFile = imageFile;
        this.size = size;
        this.purpose = purpose;
    }

    public String getIdentifier() {
        return identifier;
    }

    public int getComponentNr() {
        return componentNr;
    }

    public String getImageFile() {
        return imageFile;
    }

    public int getSize() {
        return size;
    }

    public String getPurpose() {
        return purpose;
    }
}
