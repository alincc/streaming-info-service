package no.nb.microservices.streaminginfo.core.resource.service;

import no.nb.microservices.streaminginfo.core.resource.model.MediaResource;
import no.nb.microservices.streaminginfo.core.resource.repository.MediaResourceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.Future;

/**
 * Created by andreasb on 30.09.15.
 */
@Service
public class MediaResourceService {

    private MediaResourceRepository resourcesRepository;

    @Autowired
    public MediaResourceService(MediaResourceRepository resourcesRepository) {
        this.resourcesRepository = resourcesRepository;
    }

    @Async
    public Future<List<MediaResource>> getMediafileAsync(String urn) {
        List<MediaResource> resources = resourcesRepository.findByIdentifier(urn);

        return new AsyncResult<>(resources);
    }
}
