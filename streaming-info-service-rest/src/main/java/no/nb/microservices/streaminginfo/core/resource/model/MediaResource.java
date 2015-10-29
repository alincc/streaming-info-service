package no.nb.microservices.streaminginfo.core.resource.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;


/**
 * Created by andreasb on 29.09.15.
 */
@IdClass(MediaResourceId.class)
@Entity(name = "resources")
public class MediaResource {

    @Id
    @Column(name = "identifier")
    private String identifier;

    @Id
    @Column(name = "component_no")
    private int componentNo;

    @Column(name = "imagefile")
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

    public MediaResource(String identifier, int componentNo, String imageFile, int size, String purpose) {
        this.identifier = identifier;
        this.componentNo = componentNo;
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

    public int getComponentNo() {
        return componentNo;
    }

    public void setComponentNo(int componentNo) {
        this.componentNo = componentNo;
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

