package com.custom.search;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.custom.search.service.NetflixService;

@SpringBootTest
class SearchApplicationTests {

	@Autowired
	@Qualifier("v1")
	private NetflixService netflixService;

	@Test
	void contextLoads() {
		assertNotNull(netflixService, "NetflixService bean should not be null");
	}

}
