package com.custom.search.service;

import static org.mockito.Mockito.when;

import java.util.Collections;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import com.custom.common.utilities.convertors.TypeConversionUtils;
import com.custom.search.es.entity.NetflixData;
import com.custom.search.es.repository.NetflixDataRepository;
import com.custom.search.response.SearchResponse;

import static org.junit.jupiter.api.Assertions.assertEquals;

class NetflixServiceV1ImplTest {

	@Mock
    private NetflixDataRepository netflixDataRepository;

    @InjectMocks
    private NetflixServiceV1Impl netflixService;

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

}
