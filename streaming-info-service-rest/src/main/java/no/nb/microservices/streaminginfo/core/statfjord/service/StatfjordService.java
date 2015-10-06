package no.nb.microservices.streaminginfo.core.statfjord.service;

import no.nb.microservices.streaminginfo.core.statfjord.model.StatfjordInfo;
import no.nb.microservices.streaminginfo.core.statfjord.repository.StatfjordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.Future;

/**
 * Created by andreasb on 05.10.15.
 */
@Service
public class StatfjordService implements IStatfjordService {

    @Autowired
    private StatfjordRepository statfjordRepository;

    @Override
    public List<StatfjordInfo> getStatfjordInfos() {
        return statfjordRepository.getStatfjordInfos();
    }

    @Override
    @Async
    public Future<StatfjordInfo> getStatfjordInfoAsync(String urn) {
        List<StatfjordInfo> statfjordInfos = statfjordRepository.getStatfjordInfos();
        StatfjordInfo statfjordInfo = statfjordInfos.stream().filter(q -> q.getUrn().equalsIgnoreCase(urn)).findFirst().orElse(null);
        return new AsyncResult<StatfjordInfo>(statfjordInfo);
    }
}
