package no.nb.microservices.streaminginfo.model;

/**
 * Created by andreasb on 30.09.15.
 */
public class StreamQuality {
    private String name;
    private String type;
    private String path;
    private long size;
    private Video video;
    private Audio audio;

    public StreamQuality() {
    }

    public StreamQuality(String name, String type) {
        this.name = name;
        this.type = type;
    }

    public StreamQuality(String name, String type, String path, long size, Video video, Audio audio) {
        this.name = name;
        this.type = type;
        this.path = path;
        this.size = size;
        this.video = video;
        this.audio = audio;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public Video getVideo() {
        return video;
    }

    public void setVideo(Video video) {
        this.video = video;
    }

    public Audio getAudio() {
        return audio;
    }

    public void setAudio(Audio audio) {
        this.audio = audio;
    }

    public int totalBitrate() {
        int audioBitrate = (this.getAudio() != null) ? this.getAudio().getBitrate() : 0;
        int videoBitrate = (this.getVideo() != null) ? this.getVideo().getBitrate() : 0;
        return videoBitrate + audioBitrate;
    }
}
