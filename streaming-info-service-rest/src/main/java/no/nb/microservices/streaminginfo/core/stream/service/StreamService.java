package no.nb.microservices.streaminginfo.core.stream.service;

import no.nb.microservices.catalogitem.rest.model.ItemResource;
import no.nb.microservices.streaminginfo.core.item.service.IItemService;
import no.nb.microservices.streaminginfo.core.resource.model.MediaResource;
import no.nb.microservices.streaminginfo.core.resource.service.MediaResourceService;
import no.nb.microservices.streaminginfo.core.statfjord.model.StatfjordInfo;
import no.nb.microservices.streaminginfo.core.statfjord.service.IStatfjordService;
import no.nb.microservices.streaminginfo.core.stream.exception.NoAccessException;
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

    @Autowired
    private MediaResourceService mediaResourceService;

    @Autowired
    private IItemService itemService;

    @Autowired
    private IStatfjordService statfjordService;

    public StreamInfo getStreamInfo(StreamRequest streamRequest) {
        Future<List<MediaResource>> mediaResourceFuture = mediaResourceService.getMediafileAsync(streamRequest.getUrn());
        Future<ItemResource> itemResourceFuture = itemService.getItemByUrnAsync(streamRequest.getUrn());
        Future<StatfjordInfo> statfjordInfoFuture = statfjordService.getStatfjordInfoAsync(streamRequest.getUrn());

        List<MediaResource> mediaResources;
        ItemResource itemResource;
        StatfjordInfo statfjordInfo;

        try {
            mediaResources = mediaResourceFuture.get();
            itemResource = itemResourceFuture.get();
            statfjordInfo = statfjordInfoFuture.get();
        }
        catch (InterruptedException ie) {
            throw new StreamException("Failed to fetch stream resources", ie);
        }
        catch (ExecutionException ee) {
            throw new StreamException("Failed to fetch stream resources", ee);
        }

        // Security check
        if (!"ALL".equalsIgnoreCase(itemResource.getAccessInfo().getViewability())) {
            throw new NoAccessException("User does not have access to content");
        }

        StreamInfo streamInfo = new StreamInfo(streamRequest.getUrn());

        for (MediaResource mediaResource : mediaResources) {
            Video videoInfo = new Video(0, 0, 0, null);
            Audio audioInfo = new Audio(0, null);

            StreamQuality streamQuality = new StreamQuality(FilenameUtils.getName(mediaResource.getImageFile()),
                    FilenameUtils.getExtension(mediaResource.getImageFile()),
                    mediaResource.getImageFile(),
                    mediaResource.getSize(),
                    videoInfo,
                    audioInfo);

            streamInfo.getQualities().add(streamQuality);
        }

        // If statfjord, then set statfjord specific offset and extent
        if (statfjordInfo != null) {
            streamInfo.setPlayStart(statfjordInfo.getOffset());
            streamInfo.setPlayDuration(statfjordInfo.getExtent());
        }
        else { // Else set default offset and extent
            // TODO: Set offset and extent from itemResource
            streamInfo.setPlayStart(0);
            streamInfo.setPlayDuration(0);
        }

        return streamInfo;
    }


}
