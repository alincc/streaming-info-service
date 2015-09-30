package no.nb.microservices.streaminginfo.rest.controller;

import no.nb.microservices.streaminginfo.core.security.repository.SecurityRepository;
import no.nb.microservices.streaminginfo.core.stream.service.StreamService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(MockitoJUnitRunner.class)
public class StreamControllerTest {

    private MockMvc mockMvc;

    private StreamController homeController;

    @Mock
    private StreamService streamService;

    @Mock
    private SecurityRepository securityRepository;

    @Before
    public void setup() {
        homeController = new StreamController(streamService, securityRepository);
        mockMvc = MockMvcBuilders.standaloneSetup(homeController).build();
    }

    @Test
    public void helloWorldTest() throws Exception{
        mockMvc.perform(get("/streams"))
                .andExpect(status().is4xxClientError());
    }

}
