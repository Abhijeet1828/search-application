package com.custom.search.es.repository;

import org.springframework.data.elasticsearch.core.SearchHits;

import com.custom.search.es.entity.NetflixData;

public interface ElasticsearchDao {
	
	public SearchHits<NetflixData> searchByType(String type, int page, int size);

}
