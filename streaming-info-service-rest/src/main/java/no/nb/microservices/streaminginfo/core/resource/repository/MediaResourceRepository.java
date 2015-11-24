package no.nb.microservices.streaminginfo.core.resource.repository;

import no.nb.microservices.streaminginfo.core.resource.model.MediaResource;
import no.nb.microservices.streaminginfo.core.resource.model.MediaResourceId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MediaResourceRepository extends JpaRepository<MediaResource, MediaResourceId> {
    List<MediaResource> findByIdentifier(String urn);
}
