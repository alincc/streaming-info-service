package no.nb.microservices.streaminginfo.model;

/**
 * Created by andreasb on 29.09.15.
 */
public class VideoInfo {
    private int videoWidth;
    private int videoHeight;
    private int videoBitrate;
    private String videoCodec;

    public VideoInfo() {
    }

    public VideoInfo(int videoWidth, int videoHeight, int videoBitrate, String videoCodec) {
        this.videoWidth = videoWidth;
        this.videoHeight = videoHeight;
        this.videoBitrate = videoBitrate;
        this.videoCodec = videoCodec;
    }

    public int getVideoBitrate() {
        return videoBitrate;
    }

    public void setVideoBitrate(int videoBitrate) {
        this.videoBitrate = videoBitrate;
    }

    public String getVideoCodec() {
        return videoCodec;
    }

    public void setVideoCodec(String videoCodec) {
        this.videoCodec = videoCodec;
    }

    public int getVideoHeight() {
        return videoHeight;
    }

    public void setVideoHeight(int videoHeight) {
        this.videoHeight = videoHeight;
    }

    public int getVideoWidth() {
        return videoWidth;
    }

    public void setVideoWidth(int videoWidth) {
        this.videoWidth = videoWidth;
    }
}
