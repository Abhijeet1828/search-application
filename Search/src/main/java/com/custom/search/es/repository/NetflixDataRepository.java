package com.custom.search.es.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import com.custom.search.es.entity.NetflixData;

/**
 * This interface is an elasticsearch repository for connecting to the "netflix"
 * index in elasticsearch.
 * 
 * @author Abhijeet
 *
 */
@Repository
public interface NetflixDataRepository extends ElasticsearchRepository<NetflixData, String> {

	/**
	 * This repository method is used to fetch the {@link NetflixData} list for the
	 * searched titles.
	 * 
	 * @implNote The search string can be the whole title or a part of the title.
	 *           (*searchString*)
	 * 
	 * @param title
	 * @param pageable
	 * 
	 * @return Page instance of {@link NetflixData}
	 */
	Page<NetflixData> findByTitleContainingIgnoreCase(String title, Pageable pageable);

}
