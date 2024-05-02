package com.custom.search.es.entity;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Document(indexName = "netflix", createIndex = false)
public class NetflixData {

	@Id
	private String id;
	
	@Field(type = FieldType.Text, name = "show_id")
	private String showId;

	@Field(type = FieldType.Text, name = "type")
	private String type;

	@Field(type = FieldType.Text, name = "title")
	private String title;

	@Field(type = FieldType.Text, name = "director")
	private String director;

	@Field(type = FieldType.Text, name = "cast")
	private String cast;

	@Field(type = FieldType.Text, name = "country")
	private String country;

	@Field(type = FieldType.Date, format = DateFormat.date_time, name = "date_added")
	private Date dateAdded;

	@Field(type = FieldType.Date, format = DateFormat.date_time, name = "release_year")
	private Date releaseYear;

	@Field(type = FieldType.Text, name = "rating")
	private String rating;

	@Field(type = FieldType.Text, name = "duration")
	private String duration;

	@Field(type = FieldType.Text, name = "listed_in")
	private String listedIn;

	@Field(type = FieldType.Text, name = "description")
	private String description;

	@Field(type = FieldType.Date, format = DateFormat.date_time, name = "modified_timestamp")
	private Date modifiedTimestamp;

}
