package com.custom.search.es.repository;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.Criteria;
import org.springframework.data.elasticsearch.core.query.CriteriaQuery;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.stereotype.Repository;

import com.custom.search.es.entity.NetflixData;

@Repository
public class ElasticsearchDaoImpl implements ElasticsearchDao {

	private final ElasticsearchOperations elasticsearchOperations;

	public ElasticsearchDaoImpl(ElasticsearchOperations elasticsearchOperations) {
		this.elasticsearchOperations = elasticsearchOperations;
	}

	@Override
	public SearchHits<NetflixData> searchByType(String type, int page, int size) {
		Criteria criteria = new Criteria("type").is(type);

		Query query = new CriteriaQuery(criteria, PageRequest.of(page, size));

		return elasticsearchOperations.search(query, NetflixData.class);
	}

}
