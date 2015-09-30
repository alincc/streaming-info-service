package no.nb.microservices.streaminginfo.rest.controller;

import com.netflix.discovery.converters.Auto;
import no.nb.microservices.streaminginfo.core.stream.service.StreamService;
import no.nb.microservices.streaminginfo.model.StreamInfo;
import no.nb.microservices.streaminginfo.model.StreamRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
public class StreamController {

    private static final Logger LOG = LoggerFactory.getLogger(StreamController.class);

    private final StreamService streamService;

    @Autowired
    public StreamController(StreamService streamService) {
        this.streamService = streamService;
    }

    @RequestMapping(value = "/streams", method = RequestMethod.GET)
    public ResponseEntity<StreamInfo> getStreamInfo(@Valid StreamRequest streamRequest) {
        // TODO: Check access

        // TODO: Create softlink

        return new ResponseEntity<>(streamService.getStreamInfo(streamRequest), HttpStatus.OK);
    }
}

