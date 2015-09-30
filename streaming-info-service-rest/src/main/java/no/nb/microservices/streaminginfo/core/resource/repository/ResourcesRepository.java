package no.nb.microservices.streaminginfo.core.resource.repository;

import no.nb.microservices.streaminginfo.core.resource.model.MediaResource;

import java.util.List;

/**
 *
 * @author frank
 */
public interface ResourcesRepository {

    List<MediaResource> getMediaFile(String urn);
}
