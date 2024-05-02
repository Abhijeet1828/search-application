package com.custom.search.es.repository;

import java.util.List;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import com.custom.search.es.entity.NetflixData;

@Repository
public interface NetflixDataRepository extends ElasticsearchRepository<NetflixData, String> {
	
	List<NetflixData> findByShowId(String showId);

}
