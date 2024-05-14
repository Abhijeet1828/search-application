package com.custom.search.controller;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import com.custom.common.utilities.convertors.TypeConversionUtils;
import com.custom.common.utilities.exception.CommonException;
import com.custom.common.utilities.response.CommonResponse;
import com.custom.search.response.SearchResponse;
import com.custom.search.service.NetflixService;

import static org.junit.jupiter.api.Assertions.assertEquals;

class NetflixSearchControllerV1Test {

	private NetflixService netflixService;
	private NetflixSearchControllerV1 controller;

	@BeforeEach
	void setUp() {
		netflixService = mock(NetflixService.class);
		controller = new NetflixSearchControllerV1(netflixService);
	}

	@Test
	void searchByTitle_TitleFound_Returns2000() throws CommonException {
		// Arrange
		String title = "The Matrix";
		int page = 0;
		int size = 10;
		when(netflixService.findByTitle(title, page, size)).thenReturn(mock(SearchResponse.class));

		// Act
		ResponseEntity<Object> response = controller.searchByTitle(title, page, size);
		CommonResponse commonResponse = TypeConversionUtils.convertToCustomClass(response.getBody(),
				CommonResponse.class);

		// Assert
		assertEquals(2000, commonResponse.getStatus().getStatusCode());
	}

	@Test
	void searchByTitle_TitleNotFound_ReturnsNegative2000() throws CommonException {
		// Arrange
		String title = "Non-existent title";
		int page = 0;
		int size = 10;
		when(netflixService.findByTitle(title, page, size)).thenReturn(1); // Simulating no titles found

		// Act
		ResponseEntity<Object> response = controller.searchByTitle(title, page, size);
		CommonResponse commonResponse = TypeConversionUtils.convertToCustomClass(response.getBody(),
				CommonResponse.class);

		// Assert
		assertEquals(-2000, commonResponse.getStatus().getStatusCode());
	}

	@Test
	void searchByTitle_InternalServerError_Returnsnegative1999() throws CommonException {
		// Arrange
		String title = "Internal Server Error";
		int page = 0;
		int size = 10;
		when(netflixService.findByTitle(title, page, size))
				.thenReturn(2);

		// Act
		ResponseEntity<Object> response = controller.searchByTitle(title, page, size);
		CommonResponse commonResponse = TypeConversionUtils.convertToCustomClass(response.getBody(),
				CommonResponse.class);

		// Assert
		assertEquals(-1999, commonResponse.getStatus().getStatusCode());
	}
	
	@Test
	void searchByType_TypeFound_Returns2001() throws CommonException {
		// Arrange
		String type = "Movie";
		int page = 0;
		int size = 10;
		when(netflixService.findByType(type, page, size)).thenReturn(mock(SearchResponse.class));

		// Act
		ResponseEntity<Object> response = controller.searchByType(type, page, size);
		CommonResponse commonResponse = TypeConversionUtils.convertToCustomClass(response.getBody(),
				CommonResponse.class);

		// Assert
		assertEquals(2001, commonResponse.getStatus().getStatusCode());
	}
	
	@Test
	void searchByType_TypeNotFound_ReturnsNegative2001() throws CommonException {
		// Arrange
		String type = "Movie-Series";
		int page = 0;
		int size = 10;
		when(netflixService.findByType(type, page, size)).thenReturn(1); // Simulating no titles found

		// Act
		ResponseEntity<Object> response = controller.searchByType(type, page, size);
		CommonResponse commonResponse = TypeConversionUtils.convertToCustomClass(response.getBody(),
				CommonResponse.class);

		// Assert
		assertEquals(-2001, commonResponse.getStatus().getStatusCode());
	}
	
	@Test
	void searchByType_InternalServerError_Returnsnegative1999() throws CommonException {
		// Arrange
		String type = "Internal Server Error";
		int page = 0;
		int size = 10;
		when(netflixService.findByType(type, page, size))
				.thenReturn(2);

		// Act
		ResponseEntity<Object> response = controller.searchByType(type, page, size);
		CommonResponse commonResponse = TypeConversionUtils.convertToCustomClass(response.getBody(),
				CommonResponse.class);

		// Assert
		assertEquals(-1999, commonResponse.getStatus().getStatusCode());
	}
}
