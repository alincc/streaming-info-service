package no.nb.microservices.streaminginfo.it;

import com.netflix.loadbalancer.BaseLoadBalancer;
import com.netflix.loadbalancer.ILoadBalancer;
import com.netflix.loadbalancer.Server;
import com.squareup.okhttp.mockwebserver.Dispatcher;
import com.squareup.okhttp.mockwebserver.MockResponse;
import com.squareup.okhttp.mockwebserver.MockWebServer;
import com.squareup.okhttp.mockwebserver.RecordedRequest;
import no.nb.microservices.streaminginfo.Application;
import no.nb.microservices.streaminginfo.core.resource.model.MediaResource;
import no.nb.microservices.streaminginfo.core.resource.repository.MediaResourceRepository;
import no.nb.microservices.streaminginfo.model.StreamInfo;
import no.nb.microservices.streaminginfo.model.StreamQuality;
import org.apache.commons.io.IOUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.TestRestTemplate;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.WebApplicationContext;

import java.util.Arrays;
import java.util.HashMap;

import static org.junit.Assert.assertEquals;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = {Application.class, RibbonClientConfiguration.class})
@WebIntegrationTest("server.port: 0")
public class IntegrationTest {

    @Value("${local.server.port}")
    int port;

    @Autowired
    ILoadBalancer loadBalancer;

    @Autowired
    WebApplicationContext context;

    @Autowired
    MediaResourceRepository mediaResourceRepository;

    MockMvc mockMvc;
    MockWebServer mockWebServer;
    RestTemplate rest;

    static final double DELTA = 1e-15;

    @Before
    public void setup() throws Exception {
        // Populate MediaResourceRepository
        mediaResourceRepository.deleteAll();
        MediaResource mediaResourceLq = new MediaResource("URN:NBN:no-nb_video_958", 1, "/tmp/streaming/no-nb_video_958_1280x720x4000.mp4", 2120389, "browsing");
        mediaResourceRepository.save(Arrays.asList(mediaResourceLq));

        rest = new TestRestTemplate();
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
        mockWebServer = new MockWebServer();

        // Read mock data
        String itemId1Mock = IOUtils.toString(new ClassPathResource("catalog-item-service-id1.json").getInputStream());
        String itemId2Mock = IOUtils.toString(new ClassPathResource("catalog-item-service-id2.json").getInputStream());
        String itemId3Mock = IOUtils.toString(new ClassPathResource("catalog-item-service-id3.json").getInputStream());
        String indexServiceMock = IOUtils.toString(new ClassPathResource("catalog-search-index-service.json").getInputStream());
        String indexServiceMock2 = IOUtils.toString(new ClassPathResource("catalog-search-index-service2.json").getInputStream());
        String indexServiceMock3 = IOUtils.toString(new ClassPathResource("catalog-search-index-service3.json").getInputStream());

        final Dispatcher dispatcher = new Dispatcher() {
            @Override
            public MockResponse dispatch(RecordedRequest request) throws InterruptedException {
                if (request.getPath().equals("/search?q=urn%3A%22URN%3ANBN%3Ano-nb_video_958%22&page=0&size=1")) {
                    return new MockResponse().setBody(indexServiceMock).setHeader("Content-Type", "application/hal+json; charset=utf-8");
                }
                else if (request.getPath().equals("/search?q=urn%3A%22URN%3ANBN%3Ano-nb_dra_1992-01783P%22&page=0&size=1")) {
                    return new MockResponse().setBody(indexServiceMock2).setHeader("Content-Type", "application/hal+json; charset=utf-8");
                }
                else if (request.getPath().equals("/search?q=urn%3A%22URN%3ANBN%3Ano-nb_video_959%22&page=0&size=1")) {
                    return new MockResponse().setBody(indexServiceMock3).setHeader("Content-Type", "application/hal+json; charset=utf-8");
                }
                else if (request.getPath().equals("/catalog/items/id1")) {
                    return new MockResponse().setBody(itemId1Mock).setHeader("Content-Type", "application/hal+json; charset=utf-8");
                }
                else if (request.getPath().equals("/catalog/items/id2")) {
                    return new MockResponse().setBody(itemId2Mock).setHeader("Content-Type", "application/hal+json; charset=utf-8");
                }
                else if (request.getPath().equals("/catalog/items/id3")) {
                    return new MockResponse().setBody(itemId3Mock).setHeader("Content-Type", "application/hal+json; charset=utf-8");
                }
                else {
                    return new MockResponse().setResponseCode(404);
                }
            }
        };
        mockWebServer.setDispatcher(dispatcher);
        mockWebServer.start();

        BaseLoadBalancer baseLoadBalancer = (BaseLoadBalancer) loadBalancer;
        baseLoadBalancer.setServersList(Arrays.asList(new Server(mockWebServer.getHostName(), mockWebServer.getPort())));
    }

    @After
    public void tearDown() throws Exception {
        mockWebServer.shutdown();
    }

    @Test
    public void getStreamInfoTest() {
        final HashMap<String, String> urlVariables = new HashMap<>();
        urlVariables.put("urn", "URN:NBN:no-nb_video_958");
        String uri = "http://localhost:" + port + "/streaming/streams/{urn}";
        ResponseEntity<StreamInfo> response = rest.getForEntity(uri, StreamInfo.class, urlVariables);

        assertEquals(HttpStatus.OK, response.getStatusCode());

        StreamInfo streamInfo = response.getBody();

        assertEquals("URN:NBN:no-nb_video_958", streamInfo.getUrn());
        assertEquals(120, streamInfo.getPlayDuration(), DELTA);
        assertEquals(60, streamInfo.getPlayStart(), DELTA);
        assertEquals(1, streamInfo.getQualities().size());

        StreamQuality lowQuality = streamInfo.getQualities().stream().filter(q -> q.getName().equals("no-nb_video_958_1280x720x4000.mp4")).findFirst().get();
        assertEquals("no-nb_video_958_1280x720x4000.mp4", lowQuality.getName());
        assertEquals("mp4", lowQuality.getType());
        assertEquals(2120389, lowQuality.getSize());
        assertEquals(0, lowQuality.getVideo().getBitrate());
        assertEquals(0, lowQuality.getVideo().getHeight());
        assertEquals(0, lowQuality.getVideo().getWidth());
        assertEquals(null, lowQuality.getVideo().getCodec());
        assertEquals(0, lowQuality.getAudio().getBitrate());
        assertEquals(null, lowQuality.getAudio().getCodec());
    }

    @Test
    public void getStreamInfoAccessDeniedTest() {
        final HashMap<String, String> urlVariables = new HashMap<>();
        urlVariables.put("urn", "URN:NBN:no-nb_video_959");
        String uri = "http://localhost:" + port + "/streaming/streams/{urn}";
        ResponseEntity<StreamInfo> response = rest.getForEntity(uri, StreamInfo.class, urlVariables);

        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
    }

    @Test
    public void getStatfjordStreamInfoTest() {
        final HashMap<String, String> urlVariables = new HashMap<>();
        urlVariables.put("urn", "URN:NBN:no-nb_dra_1992-01783P");
        String uri = "http://localhost:" + port + "/streaming/streams/{urn}";
        ResponseEntity<StreamInfo> response = rest.getForEntity(uri, StreamInfo.class, urlVariables);

        assertEquals(HttpStatus.OK, response.getStatusCode());

        StreamInfo streamInfo = response.getBody();

        assertEquals("URN:NBN:no-nb_dra_1992-01783P", streamInfo.getUrn());
        assertEquals(50, streamInfo.getPlayDuration(), DELTA);
        assertEquals(682, streamInfo.getPlayStart(), DELTA);
    }
}

@Configuration
class RibbonClientConfiguration {
    @Bean
    public ILoadBalancer ribbonLoadBalancer() {
        return new BaseLoadBalancer();
    }
}