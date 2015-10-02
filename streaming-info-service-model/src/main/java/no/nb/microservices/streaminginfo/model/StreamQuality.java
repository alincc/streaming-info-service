package no.nb.microservices.streaminginfo.model;

/**
 * Created by andreasb on 30.09.15.
 */
public class StreamQuality {
    private String name;
    private String type;
    private long size;
    private VideoInfo video;
    private AudioInfo audio;

    public StreamQuality() {
    }

    public StreamQuality(String name, String type) {
        this.name = name;
        this.type = type;
    }

    public StreamQuality(String name, String type, long size, VideoInfo video, AudioInfo audio) {
        this.name = name;
        this.type = type;
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

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public VideoInfo getVideo() {
        return video;
    }

    public void setVideo(VideoInfo video) {
        this.video = video;
    }

    public AudioInfo getAudio() {
        return audio;
    }

    public void setAudio(AudioInfo audio) {
        this.audio = audio;
    }

    public int totalBitrate() {
        int audioBitrate = (this.getAudio() != null) ? this.getAudio().getAudioBitrate() : 0;
        int videoBitrate = (this.getVideo() != null) ? this.getVideo().getVideoBitrate() : 0;
        return videoBitrate + audioBitrate;
    }
}
