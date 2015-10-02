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
import no.nb.sesam.ni.niclient.NiClient;
import no.nb.sesam.ni.niserver.AuthorisationHandler;
import no.nb.sesam.ni.niserver.AuthorisationHandlerResolver;
import no.nb.sesam.ni.niserver.AuthorisationRequest;
import no.nb.sesam.ni.niserver.NiServer;
import no.nb.sesam.ni.niserver.authorisation.AcceptHandler;
import no.nb.sesam.ni.niserver.authorisation.DenyHandler;
import org.apache.commons.io.IOUtils;
import org.junit.*;
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
import org.springframework.util.SocketUtils;
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

    static int TEST_SERVER_PORT;
    static String TEST_SERVER_ADDR;
    static NiServer niServer;
    static final double DELTA = 1e-15;

    @BeforeClass
    public static void setUpClass() throws Exception {
        TEST_SERVER_PORT = SocketUtils.findAvailableTcpPort();
        TEST_SERVER_ADDR = "localhost:" + TEST_SERVER_PORT;

        niServer = new NiServer(TEST_SERVER_PORT,
                new no.nb.sesam.ni.niserver.Cluster(TEST_SERVER_ADDR),
                new MockAuthorisationHandlerResolver(), null, null);
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
        niServer.shutdown(500);
    }

    @Before
    public void setup() throws Exception {
        // Populate MediaResourceRepository
        MediaResource mediaResourceLq = new MediaResource("URN:NBN:no-nb_video_958", 1, "/tmp/streaming/no-nb_video_958_1280x720x4000.mp4", 2120389, "browsing");
        mediaResourceRepository.save(Arrays.asList(mediaResourceLq));

        rest = new TestRestTemplate();
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
        mockWebServer = new MockWebServer();

        // Read mock data
        String mock1 = IOUtils.toString(new ClassPathResource("catalog-item-service-id1.json").getInputStream());

        final Dispatcher dispatcher = new Dispatcher() {
            @Override
            public MockResponse dispatch(RecordedRequest request) throws InterruptedException {
                if (request.getPath().equals("/search?q=urn%3A%22URN%3ANBN%3Ano-nb_video_958%22&page=0&size=1")) {
                    return new MockResponse().setBody(mock1).setHeader("Content-Type", "application/hal+json; charset=utf-8");
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
        setupAllowedNiServer();

        final HashMap<String, String> urlVariables = new HashMap<>();
        urlVariables.put("urn", "URN:NBN:no-nb_video_958");
        urlVariables.put("ip", "127.0.0.1");
        urlVariables.put("ssoToken", "dummyToken");
        urlVariables.put("offset", "60");
        urlVariables.put("extent", "180");
        String uri = "http://localhost:" + port + "/streams?urn={urn}&ip={ip}&ssoToken={ssoToken}&offset={offset}&extent={extent}";
        ResponseEntity<StreamInfo> response = rest.getForEntity(uri, StreamInfo.class, urlVariables);

        assertEquals(HttpStatus.OK, response.getStatusCode());

        StreamInfo streamInfo = response.getBody();

        assertEquals("URN:NBN:no-nb_video_958", streamInfo.getUrn());
        assertEquals(0, streamInfo.getPlayDuration(), DELTA);
        assertEquals(0, streamInfo.getPlayStart(), DELTA);
        assertEquals(1, streamInfo.getQualities().size());

        StreamQuality lowQuality = streamInfo.getQualities().stream().filter(q -> q.getName().equals("no-nb_video_958_1280x720x4000.mp4")).findFirst().get();
        assertEquals("no-nb_video_958_1280x720x4000.mp4", lowQuality.getName());
        assertEquals("mp4", lowQuality.getType());
        assertEquals(2120389, lowQuality.getSize());
        assertEquals(0, lowQuality.getVideo().getVideoBitrate());
        assertEquals(0, lowQuality.getVideo().getVideoHeight());
        assertEquals(0, lowQuality.getVideo().getVideoWidth());
        assertEquals(null, lowQuality.getVideo().getVideoCodec());
        assertEquals(0, lowQuality.getAudio().getAudioBitrate());
        assertEquals(null, lowQuality.getAudio().getAudioCodec());
    }

    @Test
    public void getStreamInfoDenyTest() throws Exception {
        setupDenyNiServer();

        final HashMap<String, String> urlVariables = new HashMap<>();
        urlVariables.put("urn", "URN:NBN:no-nb_video_958");
        urlVariables.put("ip", "127.0.0.1");
        urlVariables.put("ssoToken", "dummyToken");
        urlVariables.put("offset", "60");
        urlVariables.put("extent", "180");
        String uri = "http://localhost:" + port + "/streams?urn={urn}&ip={ip}&ssoToken={ssoToken}&offset={offset}&extent={extent}";
        ResponseEntity<StreamInfo> response = rest.getForEntity(uri, StreamInfo.class, urlVariables);

        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
    }

    private void setupAllowedNiServer() {
        niServer.shutdown();
        try {
            niServer = new NiServer(TEST_SERVER_PORT,
                    new no.nb.sesam.ni.niserver.Cluster(TEST_SERVER_ADDR),
                    new MockAuthorisationHandlerResolver(), null, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void setupDenyNiServer() {
        niServer.shutdown();
        try {
            niServer = new NiServer(TEST_SERVER_PORT,
                    new no.nb.sesam.ni.niserver.Cluster(TEST_SERVER_ADDR),
                    new MockAuthorisationDeniedHandlerResolver(), null, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

@Configuration
class RibbonClientConfiguration {
    @Bean
    public ILoadBalancer ribbonLoadBalancer() {
        return new BaseLoadBalancer();
    }
}

@Configuration
class TestNiConfig {

    @Bean
    public NiClient getNiClient() throws Exception {
        return new NiClient(IntegrationTest.TEST_SERVER_ADDR);
    }
}

class MockAuthorisationHandlerResolver implements AuthorisationHandlerResolver {

    public AuthorisationHandler resolve(AuthorisationRequest request) {
        return new AcceptHandler();
    }
}

class MockAuthorisationDeniedHandlerResolver implements AuthorisationHandlerResolver {

    public AuthorisationHandler resolve(AuthorisationRequest request) {
        return new DenyHandler();
    }
}


