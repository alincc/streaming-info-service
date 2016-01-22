package no.nb.microservices.streaminginfo.core.item.repository;

import no.nb.microservices.catalogitem.rest.model.ItemResource;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Created by andreasb on 13.07.15.
 */
@FeignClient("catalog-item-service")
public interface ItemRepository {

    @RequestMapping(method = RequestMethod.GET, value = "/catalog/v1/items/{id}")
    ItemResource getById(@PathVariable("id") String id);

}