package no.nb.microservices.streaminginfo.core.statfjord.repository;

import no.nb.microservices.streaminginfo.core.statfjord.model.StatfjordInfo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.List;

import static junit.framework.Assert.assertEquals;

/**
 * Created by andreasb on 05.10.15.
 */
@RunWith(MockitoJUnitRunner.class)
public class StatfjordRepositoryTest {

    @InjectMocks
    CsvStatfjordRepository csvStatfjordRepository;

    @Test
    public void getStatfjordInfosTest() {
        List<StatfjordInfo> statfjordInfos = csvStatfjordRepository.getStatfjordInfos();

        assertEquals(13, statfjordInfos.size());
        assertEquals("URN:NBN:no-nb_dra_1992-00022P", statfjordInfos.get(10).getUrn());
        assertEquals(120, statfjordInfos.get(10).getOffset());
        assertEquals(77, statfjordInfos.get(10).getExtent());
    }
}
