package no.nb.microservices.streaminginfo.core.item.service;

import no.nb.microservices.catalogitem.rest.model.ItemResource;
import no.nb.microservices.streaminginfo.core.item.repository.ItemRepository;
import no.nb.microservices.streaminginfo.core.searchindex.service.ISearchIndexService;
import no.nb.microservices.streaminginfo.core.searchindex.service.SearchIndexService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;

import java.util.concurrent.Future;

/**
 * Created by andreasb on 15.06.15.
 */
@Service
public class ItemService implements IItemService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ItemService.class);

    private ItemRepository itemRepository;
    private ISearchIndexService searchIndexService;

    @Autowired
    public ItemService(ItemRepository itemRepository, SearchIndexService searchIndexService) {
        this.itemRepository = itemRepository;
        this.searchIndexService = searchIndexService;
    }

    @Override
    @Async
    public Future<ItemResource> getItemByIdAsync(String id) {
        ItemResource itemResource = itemRepository.getById(id);
        return new AsyncResult<>(itemResource);
    }

    @Override
    @Async
    public Future<ItemResource> getItemByUrnAsync(String urn) {
        String id = searchIndexService.getId(urn);
        ItemResource itemResource = itemRepository.getById(id);

        return new AsyncResult<>(itemResource);
    }
}
