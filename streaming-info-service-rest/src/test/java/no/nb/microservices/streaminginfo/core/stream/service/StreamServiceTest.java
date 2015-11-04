package no.nb.microservices.streaminginfo.core.stream.service;

import no.nb.microservices.catalogitem.rest.model.AccessInfo;
import no.nb.microservices.catalogitem.rest.model.ItemResource;
import no.nb.microservices.catalogitem.rest.model.Metadata;
import no.nb.microservices.catalogitem.rest.model.StreamingInfo;
import no.nb.microservices.streaminginfo.core.item.service.ItemService;
import no.nb.microservices.streaminginfo.core.resource.model.MediaResource;
import no.nb.microservices.streaminginfo.core.resource.service.MediaResourceService;
import no.nb.microservices.streaminginfo.core.statfjord.model.StatfjordInfo;
import no.nb.microservices.streaminginfo.core.statfjord.service.StatfjordService;
import no.nb.microservices.streaminginfo.model.StreamInfo;
import no.nb.microservices.streaminginfo.model.StreamQuality;
import no.nb.microservices.streaminginfo.model.StreamRequest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.when;

/**
 * Created by andreasb on 30.09.15.
 */
@RunWith(MockitoJUnitRunner.class)
public class StreamServiceTest {

    private static final double DELTA = 1e-15;

    @InjectMocks
    StreamService streamService;

    @Mock
    MediaResourceService mediaResourceService;

    @Mock
    ItemService itemService;

    @Mock
    StatfjordService statfjordService;

    @Test
    public void getStreamInfoTest() {
        when(mediaResourceService.getMediafileAsync(eq("URN:NBN:no-nb_video_958"))).thenReturn(getMediaResourcesFuture());
        when(itemService.getItemByUrnAsync(eq("URN:NBN:no-nb_video_958"))).thenReturn(getItemFuture(false));
        when(statfjordService.getStatfjordInfoAsync(eq("URN:NBN:no-nb_video_958"))).thenReturn(getStatfjordInfoFuture());

        StreamRequest streamRequest = new StreamRequest("URN:NBN:no-nb_video_958");
        StreamInfo streamInfo = streamService.getStreamInfo(streamRequest);
        assertEquals("URN:NBN:no-nb_video_958", streamInfo.getUrn());
        assertEquals(120, streamInfo.getPlayDuration(), DELTA);
        assertEquals(60, streamInfo.getPlayStart(), DELTA);
        assertEquals(2, streamInfo.getQualities().size());

        StreamQuality lowQuality = streamInfo.getQualities().get(0);
        assertEquals("no-nb_video_958_1280x720x4000.mp4", lowQuality.getName());
        assertEquals("mp4", lowQuality.getType());
        assertEquals(2120389, lowQuality.getSize());
        assertEquals(0, lowQuality.getVideo().getBitrate());
        assertEquals(0, lowQuality.getVideo().getHeight());
        assertEquals(0, lowQuality.getVideo().getWidth());
        assertEquals(null, lowQuality.getVideo().getCodec());
        assertEquals(0, lowQuality.getAudio().getBitrate());
        assertEquals(null, lowQuality.getAudio().getCodec());

        StreamQuality highQuality = streamInfo.getQualities().get(1);
        assertEquals("no-nb_video_958_1920x1080x6000.mp4", highQuality.getName());
        assertEquals("mp4", highQuality.getType());
        assertEquals(3120389, highQuality.getSize());
        assertEquals(0, highQuality.getVideo().getBitrate());
        assertEquals(0, highQuality.getVideo().getHeight());
        assertEquals(0, highQuality.getVideo().getWidth());
        assertEquals(null, highQuality.getVideo().getCodec());
        assertEquals(0, highQuality.getAudio().getBitrate());
        assertEquals(null, highQuality.getAudio().getCodec());
    }

    @Test
    public void getStreamInfoWithSubUrnTest() {
        when(mediaResourceService.getMediafileAsync(eq("URN:NBN:no-nb_video_959"))).thenReturn(getMediaResourcesFuture());
        when(itemService.getItemByUrnAsync(eq("URN:NBN:no-nb_video_958"))).thenReturn(getItemFuture(true));
        when(statfjordService.getStatfjordInfoAsync(eq("URN:NBN:no-nb_video_958"))).thenReturn(getStatfjordInfoFuture());

        StreamRequest streamRequest = new StreamRequest("URN:NBN:no-nb_video_958", "URN:NBN:no-nb_video_959");
        StreamInfo streamInfo = streamService.getStreamInfo(streamRequest);
        assertEquals("URN:NBN:no-nb_video_959", streamInfo.getUrn());
        assertEquals(100, streamInfo.getPlayDuration(), DELTA);
        assertEquals(180, streamInfo.getPlayStart(), DELTA);
    }

    private Future<StatfjordInfo> getStatfjordInfoFuture() {
        return new Future<StatfjordInfo>() {
            @Override
            public boolean cancel(boolean mayInterruptIfRunning) {
                return false;
            }

            @Override
            public boolean isCancelled() {
                return false;
            }

            @Override
            public boolean isDone() {
                return false;
            }

            @Override
            public StatfjordInfo get() throws InterruptedException, ExecutionException {
                return null;
            }

            @Override
            public StatfjordInfo get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
                return null;
            }
        };
    }

    private Future<List<MediaResource>> getMediaResourcesFuture() {
        MediaResource mediaResourceLq = new MediaResource("URN:NBN:no-nb_video_958", 1, "/tmp/streaming/no-nb_video_958_1280x720x4000.mp4", 2120389, "browsing");
        MediaResource mediaResourceHq = new MediaResource("URN:NBN:no-nb_video_958", 1, "/tmp/streaming/no-nb_video_958_1920x1080x6000.mp4", 3120389, "browsing");
        List<MediaResource> mediaResources = Arrays.asList(mediaResourceLq, mediaResourceHq);
        return new Future<List<MediaResource>>() {
            @Override
            public boolean cancel(boolean mayInterruptIfRunning) {
                return false;
            }

            @Override
            public boolean isCancelled() {
                return false;
            }

            @Override
            public boolean isDone() {
                return false;
            }

            @Override
            public List<MediaResource> get() throws InterruptedException, ExecutionException {
                return mediaResources;
            }

            @Override
            public List<MediaResource> get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
                return mediaResources;
            }
        };
    }

    private Future<ItemResource> getItemFuture(boolean multipleStreamingInfos) {
        ItemResource itemResource = new ItemResource();
        Metadata metadata = new Metadata();
        AccessInfo accessInfo = new AccessInfo();
        accessInfo.setAccessAllowedFrom("EVERYWHERE");
        accessInfo.setDigital(true);
        accessInfo.setPublicDomain(true);
        accessInfo.setViewability("ALL");
        itemResource.setAccessInfo(accessInfo);
        itemResource.setMetadata(metadata);
        StreamingInfo streamingInfo = new StreamingInfo("URN:NBN:no-nb_video_958", 60, 120);
        StreamingInfo streamingInfo2 = new StreamingInfo("URN:NBN:no-nb_video_959", 180, 100);

        if (multipleStreamingInfos) {
            metadata.setStreamingInfo(Arrays.asList(streamingInfo, streamingInfo2));
        }
        else {
            metadata.setStreamingInfo(Arrays.asList(streamingInfo));
        }

        return new Future<ItemResource>() {
            @Override
            public boolean cancel(boolean mayInterruptIfRunning) {
                return false;
            }

            @Override
            public boolean isCancelled() {
                return false;
            }

            @Override
            public boolean isDone() {
                return false;
            }

            @Override
            public ItemResource get() throws InterruptedException, ExecutionException {
                return itemResource;
            }

            @Override
            public ItemResource get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
                return itemResource;
            }
        };
    }
}
