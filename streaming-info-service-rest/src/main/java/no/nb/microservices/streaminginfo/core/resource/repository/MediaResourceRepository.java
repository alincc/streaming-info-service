package no.nb.microservices.streaminginfo.core.resource.repository;

import no.nb.microservices.streaminginfo.core.resource.model.MediaResource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by andreasb on 01.10.15.
 */
@Repository
public interface MediaResourceRepository extends JpaRepository<MediaResource, Integer> {
    List<MediaResource> findByIdentifier(String urn);
}
