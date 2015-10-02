package no.nb.microservices.streaminginfo.core.resource.service;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.command.AsyncResult;
import no.nb.microservices.streaminginfo.core.resource.model.MediaResource;
import no.nb.microservices.streaminginfo.core.resource.repository.MediaResourceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
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

    @HystrixCommand(fallbackMethod = "getDefaultMediafile")
    public Future<List<MediaResource>> getMediafileAsync(String urn) {
        return new AsyncResult<List<MediaResource>>() {
            @Override
            public List<MediaResource> invoke() {
                return resourcesRepository.findByIdentifier(urn);
            }
        };
    }

    private List<MediaResource> getDefaultMediafile(String urn) {
        MediaResource mediaResourceLq = new MediaResource("URN:NBN:no-nb_video_958", 1, "/tmp/streaming/no-nb_video_958_1280x720x4000.mp4", 2120389, "1280x720x4000");
        MediaResource mediaResourceHq = new MediaResource("URN:NBN:no-nb_video_958", 1, "/tmp/streaming/no-nb_video_958_1920x1080x6000.mp4", 3120389, "1920x1080x6000");

        return Arrays.asList(mediaResourceHq, mediaResourceLq);
    }
}
