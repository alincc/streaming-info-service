package no.nb.microservices.streaminginfo.core.resource.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;


/**
 * Created by andreasb on 29.09.15.
 */
@Entity
public class MediaResource {

    @Id
    @Column(name = "identifier")
    private String identifier;

    @Column(name = "component_nr")
    private int componentNr;

    @Column(name = "imageFile")
    private String imageFile;

    @Column(name = "size")
    private int size;

    @Column(name = "md5sum")
    private String md5Sum;

    @Column(name = "creation_time")
    private String creationTime;

    @Column(name = "purpose")
    private String purpose;

    public MediaResource() {
    }

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

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public int getComponentNr() {
        return componentNr;
    }

    public void setComponentNr(int componentNr) {
        this.componentNr = componentNr;
    }

    public String getImageFile() {
        return imageFile;
    }

    public void setImageFile(String imageFile) {
        this.imageFile = imageFile;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public String getMd5Sum() {
        return md5Sum;
    }

    public void setMd5Sum(String md5Sum) {
        this.md5Sum = md5Sum;
    }

    public String getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(String creationTime) {
        this.creationTime = creationTime;
    }

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }
}
