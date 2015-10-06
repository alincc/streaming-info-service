package no.nb.microservices.streaminginfo.core.statfjord.service;

import no.nb.microservices.streaminginfo.core.statfjord.model.StatfjordInfo;

import java.util.List;
import java.util.concurrent.Future;

/**
 * Created by andreasb on 05.10.15.
 */
public interface IStatfjordService {
    List<StatfjordInfo> getStatfjordInfos();

    Future<StatfjordInfo> getStatfjordInfoAsync(String urn);
}
