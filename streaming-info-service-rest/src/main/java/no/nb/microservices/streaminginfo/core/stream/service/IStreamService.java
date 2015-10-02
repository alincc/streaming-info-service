package no.nb.microservices.streaminginfo.core.stream.service;

import no.nb.microservices.streaminginfo.model.StreamInfo;
import no.nb.microservices.streaminginfo.model.StreamRequest;

/**
 * Created by andreasb on 01.10.15.
 */
public interface IStreamService {
    StreamInfo getStreamInfo(StreamRequest streamRequest);
}
