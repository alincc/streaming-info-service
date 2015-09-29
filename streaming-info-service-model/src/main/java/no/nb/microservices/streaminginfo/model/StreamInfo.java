package no.nb.microservices.streaminginfo.model;

/**
 * Created by andreasb on 29.09.15.
 */
public class StreamInfo {
    private String name;
    private String type;
    private double playDuration;
    private double playStart;
    private long size;
    private VideoInfo video;
    private AudioInfo audio;

    public StreamInfo(String name, String type) {
        this.name = name;
        this.type = type;
    }

    public StreamInfo(String name, String type, double playDuration, double playStart, long size, VideoInfo video, AudioInfo audio) {
        this.name = name;
        this.type = type;
        this.playDuration = playDuration;
        this.playStart = playStart;
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

    public double getPlayDuration() {
        return playDuration;
    }

    public void setPlayDuration(double playDuration) {
        this.playDuration = playDuration;
    }

    public double getPlayStart() {
        return playStart;
    }

    public void setPlayStart(double playStart) {
        this.playStart = playStart;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public int totalBitrate() {
        int audioBitrate = (this.getAudio() != null) ? this.getAudio().getAudioBitrate() : 0;
        int videoBitrate = (this.getVideo() != null) ? this.getVideo().getVideoBitrate() : 0;
        return videoBitrate + audioBitrate;
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
}
