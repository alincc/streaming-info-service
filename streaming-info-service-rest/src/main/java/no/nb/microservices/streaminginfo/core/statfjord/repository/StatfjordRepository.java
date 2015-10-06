package no.nb.microservices.streaminginfo.core.statfjord.repository;

import no.nb.microservices.streaminginfo.core.statfjord.model.StatfjordInfo;

import java.util.List;

/**
 * Created by andreasb on 05.10.15.
 */
public interface StatfjordRepository {
    List<StatfjordInfo> getStatfjordInfos();
}
