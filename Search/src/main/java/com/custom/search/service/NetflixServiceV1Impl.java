package com.custom.search.service;

import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.custom.search.es.entity.NetflixData;
import com.custom.search.es.repository.NetflixDataRepository;
import com.custom.search.response.SearchResponse;

/**
 * This service layer is used to implement the methods required to interact with
 * the Netlfix index.
 * 
 * @version V1
 * 
 * @author Abhijeet
 *
 */
@Service
@Qualifier(value = "v1")
public class NetflixServiceV1Impl implements NetflixService {

	private static final Logger LOGGER = LoggerFactory.getLogger(NetflixServiceV1Impl.class);

	private final NetflixDataRepository netflixDataRepository;

	@Autowired
	public NetflixServiceV1Impl(NetflixDataRepository netflixDataRepository) {
		this.netflixDataRepository = netflixDataRepository;
	}

	@Override
	public Object findByTitle(String title, int page, int size) {
		try {
			Page<NetflixData> pageNetflixData = netflixDataRepository.findByTitleContainingIgnoreCase(title,
					PageRequest.of(page, size));

			if (CollectionUtils.isEmpty(pageNetflixData.getContent())) {
				return 1;
			}

			return SearchResponse.builder().data(pageNetflixData.getContent())
					.numberOfPages(pageNetflixData.getTotalPages()).totalElements(pageNetflixData.getTotalElements())
					.build();

		} catch (Exception e) {
			LOGGER.error("Exception in findByTitle", e);
			return 2;
		}
	}

}
