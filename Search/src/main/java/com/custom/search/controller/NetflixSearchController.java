package com.custom.search.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.custom.common.utilities.response.ResponseHelper;
import com.custom.search.es.entity.NetflixData;
import com.custom.search.es.repository.NetflixDataRepository;

@RestController
@RequestMapping(value = "/netflix")
public class NetflixSearchController {

	@Autowired
	private NetflixDataRepository netflixDataRepository;

	@GetMapping(value = "/name/{id}")
	public ResponseEntity<Object> findByName(@PathVariable String id) {
		List<NetflixData> data = netflixDataRepository.findByShowId(id);

		return ResponseHelper.generateResponse(200, "Found", data);

	}

}
