package no.nb.microservices.streaminginfo.core.statfjord.repository;

import no.nb.microservices.streaminginfo.core.statfjord.model.StatfjordInfo;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Repository;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by andreasb on 05.10.15.
 */
@Repository
public class CsvStatfjordRepository implements StatfjordRepository {

    private static final Logger LOG = LoggerFactory.getLogger(CsvStatfjordRepository.class);

    @Override
    @Cacheable("statfjordInfos")
    public List<StatfjordInfo> getStatfjordInfos() {
        List<StatfjordInfo> statfjordInfos = new ArrayList<>();

        try {
            CSVParser parser = new CSVParser(new FileReader(new ClassPathResource("statfjord.csv").getFile()), CSVFormat.DEFAULT.withHeader());

            for (CSVRecord record : parser) {
                statfjordInfos.add(new StatfjordInfo(record.get("URN"), Integer.parseInt(record.get("OFFSET")), Integer.parseInt(record.get("EXTENT"))));
            }
        }
        catch (FileNotFoundException fnfe) {
            LOG.error("statfjord.csv not found", fnfe);
        }
        catch (IOException ioe) {
            LOG.error("Failed to read statfjord.csv file", ioe);
        }

        return statfjordInfos;
    }
}
