package com.custom.search.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.custom.common.utilities.exception.CommonException;
import com.custom.common.utilities.response.CommonResponse;
import com.custom.common.utilities.response.ResponseHelper;
import com.custom.search.constants.FailureConstants;
import com.custom.search.constants.SuccessConstants;
import com.custom.search.response.SearchResponse;
import com.custom.search.service.NetflixService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

/**
 * This controller is used to send search requests to Netflix index in the
 * elasticsearch.
 * 
 * @version V1
 * 
 * @author Abhijeet
 *
 */
@RestController
@RequestMapping(value = "/v1/netflix")
public class NetflixSearchControllerV1 {

	private final NetflixService netflixService;

	@Autowired
	public NetflixSearchControllerV1(@Qualifier(value = "v1") NetflixService netflixService) {
		this.netflixService = netflixService;
	}

	/**
	 * This controller method is used to search for Netflix titles using the given
	 * search string.
	 * 
	 * @param title
	 * @param page
	 * @param size
	 * 
	 * @return {@link SearchResponse}
	 * 
	 * @throws CommonException
	 */
	@Tag(name = "get", description = "GET methods of Netflix Controller")
	@Operation(summary = "Fetch the netflix titles", description = "The API fetches the titles matching the provided title string.")
	@ApiResponse(responseCode = "2000", description = "Titles Found", content = {
			@Content(mediaType = "application/json", schema = @Schema(implementation = SearchResponse.class)) })
	@ApiResponse(responseCode = "-2000", description = "No titles Found", content = {
			@Content(schema = @Schema(implementation = CommonResponse.class)) })
	@GetMapping(value = "/title/{title}/{page}/{size}")
	public ResponseEntity<Object> searchByTitle(@NotBlank @PathVariable String title,
			@PositiveOrZero @PathVariable int page, @Positive @PathVariable int size) {
		Object response = netflixService.findByTitle(title, page, size);

		if (response instanceof Integer i) {
			if (i == 1)
				return ResponseHelper.generateResponse(FailureConstants.NO_TITLES_FOUND.getFailureCode(),
						FailureConstants.NO_TITLES_FOUND.getFailureMsg());
			else
				return ResponseHelper.generateResponse(
						new CommonResponse(FailureConstants.INTERNAL_SERVER_ERROR.getFailureCode(),
								FailureConstants.INTERNAL_SERVER_ERROR.getFailureMsg()),
						HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return ResponseHelper.generateResponse(SuccessConstants.FIND_BY_TITLE.getSuccessCode(),
				SuccessConstants.FIND_BY_TITLE.getSuccessMsg(), response);
	}

}
