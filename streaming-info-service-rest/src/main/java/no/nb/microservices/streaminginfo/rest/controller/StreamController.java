package no.nb.microservices.streaminginfo.rest.controller;

import no.nb.microservices.streaminginfo.core.stream.service.StreamService;
import no.nb.microservices.streaminginfo.model.StreamInfo;
import no.nb.microservices.streaminginfo.model.StreamRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/streaming")
public class StreamController {

    private static final Logger LOG = LoggerFactory.getLogger(StreamController.class);

    private final StreamService streamService;

    @Autowired
    public StreamController(StreamService streamService) {
        this.streamService = streamService;
    }

    @RequestMapping(value = "/streams/{urn}", method = RequestMethod.GET)
    public ResponseEntity<StreamInfo> getStreamInfo(@PathVariable String urn, @RequestParam(required = false, defaultValue = "") String site) {
        return new ResponseEntity<>(streamService.getStreamInfo(new StreamRequest(urn, "", site)), HttpStatus.OK);
    }

    @RequestMapping(value = "/streams/{urn}/{subUrn}", method = RequestMethod.GET)
    public ResponseEntity<StreamInfo> getStreamInfoWithSubUrn(@PathVariable String urn, @PathVariable String subUrn, @RequestParam(required = false, defaultValue = "") String site) {
        return new ResponseEntity<>(streamService.getStreamInfo(new StreamRequest(urn, subUrn, site)), HttpStatus.OK);
    }
}

