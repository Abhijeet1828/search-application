package com.custom.search.response;

import java.io.Serializable;
import java.util.List;

import com.custom.search.es.entity.NetflixData;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * This class is used to send the search responses for the Netflix index.
 * 
 * @author Abhijeet
 *
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SearchResponse implements Serializable {

	private static final long serialVersionUID = -3877112615672310687L;

	private List<NetflixData> data;

	private int numberOfPages;

	private long totalElements;

}
