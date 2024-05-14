package com.custom.search.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.SearchHits;

import com.custom.common.utilities.convertors.TypeConversionUtils;
import com.custom.search.es.entity.NetflixData;
import com.custom.search.es.repository.ElasticsearchDao;
import com.custom.search.es.repository.NetflixDataRepository;
import com.custom.search.response.SearchResponse;

class NetflixServiceV1ImplTest {

	@Mock
	private NetflixDataRepository netflixDataRepository;

	@InjectMocks
	private NetflixServiceV1Impl netflixService;

	@Mock
	private ElasticsearchDao elasticsearchDao;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	void testFindByTitle_WithValidTitle_ReturnsSearchResponse() {
		// Arrange
		String title = "House of Cards";
		int page = 0;
		int size = 10;
		NetflixData netflixData = new NetflixData();
		netflixData.setTitle(title);
		Page<NetflixData> pageNetflixData = new PageImpl<>(Collections.singletonList(netflixData));
		when(netflixDataRepository.findByTitleContainingIgnoreCase(title, PageRequest.of(page, size)))
				.thenReturn(pageNetflixData);

		// Act
		Object result = netflixService.findByTitle(title, page, size);

		SearchResponse searchResponse = TypeConversionUtils.convertToCustomClass(result, SearchResponse.class);
		// Assert
		assertEquals(title, searchResponse.getData().get(0).getTitle());
	}

	@Test
	void testFindByTitle_WithEmptyResult_ReturnsInteger1() {
		// Arrange
		String title = "Non-existent Title";
		int page = 0;
		int size = 10;
		Page<NetflixData> pageNetflixData = new PageImpl<>(Collections.emptyList());
		when(netflixDataRepository.findByTitleContainingIgnoreCase(title, PageRequest.of(page, size)))
				.thenReturn(pageNetflixData);

		// Act
		Object result = netflixService.findByTitle(title, page, size);

		// Assert
		assertEquals(1, result);
	}

	@Test
	void testFindByTitle_WithException_ReturnsInteger2() {
		// Arrange
		String title = "Breaking Bad";
		int page = 0;
		int size = 10;
		when(netflixDataRepository.findByTitleContainingIgnoreCase(title, PageRequest.of(page, size)))
				.thenThrow(new RuntimeException("Some error occurred"));

		// Act
		Object result = netflixService.findByTitle(title, page, size);

		// Assert
		assertEquals(2, result);
	}

	@SuppressWarnings("unchecked")
	@Test
	void findByType_Successful() {
		// Mock ElasticsearchDao searchByType method to return searchHits
		String type = "movie";
		int page = 0;
		int size = 10;
		NetflixData netflixData = new NetflixData();
		netflixData.setTitle("House of Cards");
		List<NetflixData> movieList = new ArrayList<>();
		movieList.add(netflixData);

		SearchHits<NetflixData> searchHits = mock(SearchHits.class);
		when(elasticsearchDao.searchByType(type, page, size)).thenReturn(searchHits);

		// Mock empty searchHits
		when(searchHits.isEmpty()).thenReturn(false);

		// Mock search results
		List<NetflixData> searchResults = new ArrayList<>();
		// Add mock NetflixData objects to searchResults if needed

		// Mock searchHits stream method to return searchResults
		//when(searchHits.stream().map(SearchHit::getContent)).thenReturn(movieList.stream());

		// Call the findByType method
		Object result = netflixService.findByType(type, page, size);

		// Verify that the result is a SearchResponse object with correct data
		assertTrue(result instanceof SearchResponse);
		SearchResponse searchResponse = (SearchResponse) result;
		assertEquals(searchResults, searchResponse.getData());
		// Add more assertions if needed
	}

	@SuppressWarnings("unchecked")
	@Test
	void findByType_EmptySearch() {
		// Mock ElasticsearchDao searchByType method to return empty searchHits
		String type = "movie";
		int page = 0;
		int size = 10;
		SearchHits<NetflixData> searchHits = mock(SearchHits.class);
		when(elasticsearchDao.searchByType(type, page, size)).thenReturn(searchHits);

		// Mock empty searchHits
		when(searchHits.isEmpty()).thenReturn(true);

		// Call the findByType method
		Object result = netflixService.findByType(type, page, size);

		// Verify that the result is 1 indicating no data found
		assertTrue(result instanceof Integer);
		assertEquals(1, (int) result);
	}

	@Test
	void findByType_Exception() {
		// Mock ElasticsearchDao searchByType method to throw an exception
		String type = "movie";
		int page = 0;
		int size = 10;
		when(elasticsearchDao.searchByType(type, page, size)).thenThrow(new RuntimeException("Mock Exception"));

		// Call the findByType method
		Object result = netflixService.findByType(type, page, size);

		// Verify that the result is 2 indicating an error occurred
		assertTrue(result instanceof Integer);
		assertEquals(2, (int) result);
	}

}
