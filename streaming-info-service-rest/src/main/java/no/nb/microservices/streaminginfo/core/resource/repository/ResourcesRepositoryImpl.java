package no.nb.microservices.streaminginfo.core.resource.repository;

import no.nb.microservices.streaminginfo.core.resource.model.MediaResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *
 * @author frank
 */
@Repository
public class ResourcesRepositoryImpl implements ResourcesRepository {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public ResourcesRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<MediaResource> getMediaFile(String urn) {
        String query = "select identifier, imageFile, purpose, component_no, size from resources where identifier=?";
        try {
            List<Map<String, Object>> queryForList = jdbcTemplate.queryForList(query,urn);
            List<MediaResource> mediaFiles = new ArrayList<>();
            for (Map<String, Object> row: queryForList) {

                String id = (String) row.get("identifier");
            	String imageFile = (String) row.get("imageFile");
            	String purpose = (String) row.get("purpose");
            	int componentNo = (Integer) row.get("component_no");
            	int size = (Integer) row.get("size");
                MediaResource f = new MediaResource(id, componentNo, imageFile, size, purpose);
                mediaFiles.add(f);
            }
            return mediaFiles;
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }
}
