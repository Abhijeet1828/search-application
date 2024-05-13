package com.custom.search.service;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.custom.search.es.repository.ElasticsearchDao;
import com.custom.search.es.repository.NetflixDataRepository;

/**
 * This service layer is used to create V2 version of the methods declared in
 * {@link NetflixService}.
 * 
 * @version V2
 * 
 * @author Abhijeet
 *
 */
@Service
@Qualifier(value = "v2")
public class NetflixServiceV2Impl extends NetflixServiceV1Impl {

	public NetflixServiceV2Impl(NetflixDataRepository netflixDataRepository, ElasticsearchDao elasticsearchDao) {
		super(netflixDataRepository, elasticsearchDao);
	}

}
