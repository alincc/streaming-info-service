package no.nb.microservices.streaminginfo.core.stream.service;

import no.nb.microservices.catalogitem.rest.model.ItemResource;
import no.nb.microservices.streaminginfo.core.item.service.IItemService;
import no.nb.microservices.streaminginfo.core.resource.model.MediaResource;
import no.nb.microservices.streaminginfo.core.resource.service.MediaResourceService;
import no.nb.microservices.streaminginfo.core.stream.exception.StreamException;
import no.nb.microservices.streaminginfo.model.*;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * Created by andreasb on 29.09.15.
 */
@Service
public class StreamService implements IStreamService {

    private final MediaResourceService mediaResourceService;
    private final IItemService itemService;

    @Autowired
    public StreamService(MediaResourceService mediaResourceService, IItemService itemService) {
        this.mediaResourceService = mediaResourceService;
        this.itemService = itemService;
    }

    public StreamInfo getStreamInfo(StreamRequest streamRequest) {
        Future<List<MediaResource>> mediaResourceFuture = mediaResourceService.getMediafileAsync(streamRequest.getUrn());
        Future<ItemResource> itemResourceFuture = itemService.getItemByUrnAsync(streamRequest.getUrn());

        List<MediaResource> mediaResources;
        ItemResource itemResource;

        try {
            mediaResources = mediaResourceFuture.get();
            itemResource = itemResourceFuture.get();
        }
        catch (InterruptedException ie) {
            throw new StreamException("Failed to fetch stream resources", ie);
        }
        catch (ExecutionException ee) {
            throw new StreamException("Failed to fetch stream resources", ee);
        }

        StreamInfo streamInfo = new StreamInfo(streamRequest.getUrn());

        for (MediaResource mediaResource : mediaResources) {
            VideoInfo videoInfo = new VideoInfo(0, 0, 0, null);
            AudioInfo audioInfo = new AudioInfo(0, null);

            StreamQuality streamQuality = new StreamQuality(FilenameUtils.getName(mediaResource.getImageFile()),
                    FilenameUtils.getExtension(mediaResource.getImageFile()),
                    mediaResource.getSize(),
                    videoInfo,
                    audioInfo);

            streamInfo.getQualities().add(streamQuality);
        }

        // TODO: Set offset and extent from itemResource
        streamInfo.setPlayStart(0);
        streamInfo.setPlayDuration(0);

        return streamInfo;
    }


}
