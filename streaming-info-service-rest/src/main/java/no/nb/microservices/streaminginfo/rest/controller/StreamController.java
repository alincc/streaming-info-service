package no.nb.microservices.streaminginfo.rest.controller;

import no.nb.microservices.streaminginfo.core.security.repository.SecurityRepository;
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

@RestController
public class StreamController {

    private static final Logger LOG = LoggerFactory.getLogger(StreamController.class);

    private final StreamService streamService;
    private final SecurityRepository securityRepository;

    @Autowired
    public StreamController(StreamService streamService, SecurityRepository securityRepository) {
        this.streamService = streamService;
        this.securityRepository = securityRepository;
    }

    @RequestMapping(value = "/streams", method = RequestMethod.GET)
    public ResponseEntity<StreamInfo> getStreamInfo(StreamRequest streamRequest) {
        boolean hasAccess = securityRepository.hasAccess(streamRequest.getUrn(), streamRequest.getIp(), streamRequest.getSsoToken());
        if (!hasAccess) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        return new ResponseEntity<>(streamService.getStreamInfo(streamRequest), HttpStatus.OK);
    }
}

