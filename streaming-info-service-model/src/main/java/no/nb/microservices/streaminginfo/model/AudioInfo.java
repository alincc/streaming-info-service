package no.nb.microservices.streaminginfo.model;

/**
 * Created by andreasb on 29.09.15.
 */
public class AudioInfo {
    private int audioBitrate;
    private String audioCodec;

    public int getAudioBitrate() {
        return audioBitrate;
    }

    public void setAudioBitrate(int audioBitrate) {
        this.audioBitrate = audioBitrate;
    }

    public String getAudioCodec() {
        return audioCodec;
    }

    public void setAudioCodec(String audioCodec) {
        this.audioCodec = audioCodec;
    }
}
