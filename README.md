<a name="top"></a>

<h1 align="center">
:mag_right: Search Application :mag:
</h1>
<h2 align="center">
<em>Spring Boot with Elasticsearch ( ELK Stack )</em>
</h2>
<br/>

## 1. Introduction

The search application leverages the robust capabilities of Spring Boot to seamlessly interface with an Elasticsearch instance, facilitating a spectrum of operations ranging from search queries to data manipulation tasks such as addition, modification, and deletion within indexes. Integral to the application's architecture is the ELK (Elasticsearch, Logstash, Kibana) stack, meticulously integrated to provide comprehensive support to the Spring Boot framework.

At the core of data synchronization lies Logstash, orchestrating periodic data transfers from the MySQL database to Elasticsearch, ensuring real-time accessibility and synchronization of information. This pivotal mechanism guarantees the freshness and accuracy of data presented to end-users.

Furthermore, the application interfaces seamlessly with SonarQube, an industry-leading platform, for diligent verification of test coverage and identification of potential coding issues. This meticulous scrutiny, facilitated by SonarQube, enhances the code quality and fosters the development of robust, maintainable software solutions.

In alignment with contemporary best practices, the application harnesses the power of JUnit 5, a sophisticated testing framework, for crafting comprehensive test suites. This choice underscores a commitment to rigorous testing methodologies, ensuring the reliability and resilience of the deployed software.

Collectively, this sophisticated ecosystem of technologies harmonizes seamlessly to underpin the search application, empowering it to deliver unparalleled performance, reliability, and maintainability.

<table align="center">
  <tr>
    <th width="33px">Tools</td>
    <th width="45px">Version</td>
  </tr>
  <tr>
    <td>Spring Boot</td>
    <td>3.3.0-SNAPSHOT</td>
  </tr>
  <tr>
    <td>Java</td>
    <td>22</td>
  </tr>
  <tr>
    <td>Elasticsearch</td>
    <td>8.13.2</td>
  </tr>
  <tr>
    <td>Kibana</td>
    <td>8.13.2</td>
  </tr>
  <tr>
    <td>Logstash</td>
    <td>8.13.2</td>
  </tr>
  <tr>
    <td>MySQL</td>
    <td>8.3.0</td>
  </tr>
  <tr>
    <td>JUnit</td>
    <td>5.10.2</td>
  </tr>
  <tr>
    <td>Sonarqube</td>
    <td>10.5</td>
  </tr>
</table>

***
***

## 2. MySQL Configurations

This section talks about downloading MySQL server and performing actions such as creating database, creating tables and inserting data into the tables. All this is necessary for the further configuration of the Spring Boot application and ELK stack. 

1. First, download and install a MySQL server from the official [MySQL website](https://dev.mysql.com/downloads/mysql/).
2. After installation and setting the root password, login into the MySQL server using the below command and enter the password.
```SQL
mysql -u root -p
```
3. Once you have logged in, create a database using the below command. Replace _<<database-name>>_ with your database name.
```SQL
CREATE DATABASE <<database-name>>
```
4. After creating the database, navigate into the database by using the below command.
```SQL
USE <<database-name>>
```
5. Once you have navigated to the database, we can start creating the tables using the below command. You can change the columns and names according to your needs. I also altered the table to add an additional column **_modified_timestamp_** which will be used later on in **Logstash configuration**. 
```SQL
CREATE TABLE netflix_data(
   show_id      VARCHAR(30) NOT NULL PRIMARY KEY,
   type         VARCHAR(100),
   title        VARCHAR(1000),
   director     VARCHAR(500),
   cast         LONGTEXT,
   country      VARCHAR(100),
   date_added   DATE,
   release_year YEAR,
   rating       VARCHAR(50),
   duration     VARCHAR(1000),
   listed_in    MEDIUMTEXT,
   description  LONGTEXT);

ALTER TABLE netflix_data ADD COLUMN modified_timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP;
```
6. Once you have created the tables, we can start inserting the data into the table. I have used [Kaggle - Netflix Movies and TV Shows](https://www.kaggle.com/code/shivamb/netflix-shows-and-movies-exploratory-analysis) for inserting test data into my table. There are two ways to insert data into the tables.
  - First, you can directly insert the rows by running the below command in MySQL terminal instance. 
```SQL
INSERT INTO netflix_data(show_id,type,title,director,cast,country,date_added,release_year,rating,duration,listed_in,description) VALUES ('s1','Movie','Dick Johnson Is Dead','Kirsten Johnson',NULL,'United States','2021-09-25','2020','PG-13','90 min','Documentaries','As her father nears the end of his life, filmmaker Kirsten Johnson stages his death in inventive and comical ways to help them both face the inevitable.');

INSERT INTO netflix_data(show_id,type,title,director,cast,country,date_added,release_year,rating,duration,listed_in,description) VALUES ('s2','TV Show','Blood & Water',NULL,'Ama Qamata, Khosi Ngema, Gail Mabalane, Thabang Molaba, Dillon Windvogel, Natasha Thahane, Arno Greeff, Xolile Tshabalala, Getmore Sithole, Cindy Mahlangu, Ryle De Morny, Greteli Fincham, Sello Maake Ka-Ncube, Odwa Gwanya, Mekaila Mathys, Sandi Schultz, Duane Williams, Shamilla Miller, Patrick Mofokeng','South Africa','2021-09-24','2021','TV-MA','2 Seasons','International TV Shows, TV Dramas, TV Mysteries','After crossing paths at a party, a Cape Town teen sets out to prove whether a private-school swimming star is her sister who was abducted at birth.');
```
  - Second, you can add all the insert statements in a .sql file and run all the insert queries using the below command. The SQL file which is used in this instance can be found [here.](Database_Files/netflix_insert.sql)
```SQL
source <<path-to-sql-file>>
```

***
***

## 3. Confguring ELK Stack

This section will talk about how to install and configure the ELK stack. There are two main things, how to connect Elasticsearch and Kibana, configuring Logstash to periodically scrape data from MySQL and insert into Elasticsearch.

<a name="configuring-elasticsearch"></a>
### 3.1 Configuring Elasticsearch

1. Firstly, download Elasticsearch from the official [Elastic website.](https://www.elastic.co/downloads/elasticsearch)
2. Navigate to **_/config/elasticsearch.yml_** file and add the below statement before running the elasticsearch instance.
```
xpack.ml.enabled: false
```
3. Then, navigate to the **_/bin_** folder, and run the below command for starting the elasticsearch instance.
```script
./elasticsearch
```
3. During the execution of the above, copy the elasticsearch username, password, enrollment token for Kibana and SHA-256 fingerprint of the certificate. This will be used in further steps to configure Kibana and create a connection between Spring Boot application. It should look something like below:

```
━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
✅ Elasticsearch security features have been automatically configured!
✅ Authentication is enabled and cluster connections are encrypted.

ℹ️  Password for the elastic user (reset with `bin/elasticsearch-reset-password -u elastic`):
  I2VSOAmx4Xu-bRR5ZqmG

ℹ️  HTTP CA certificate SHA-256 fingerprint:
  f9f3cc23489d84642a1f129703ebbb2cabfa918eb56da70a1d9cd67a534f75d1

ℹ️  Configure Kibana to use this cluster:
• Run Kibana and click the configuration link in the terminal when Kibana starts.
• Copy the following enrollment token and paste it into Kibana in your browser (valid for the next 30 minutes):
  eyJ2ZXIiOiI4LjEzLjIiLCJhZHIiOlsiMTAwLjEwMy4xMTkuNTI6OTIwMCJdLCJmZ3IiOiJmOWYzY2MyMzQ4OWQ4NDY0MmExZjEyOTcwM2ViYmIyY2FiZmE5MThlYjU2ZGE3MGExZDljZDY3YTUzNGY3NWQxIiwia2V5IjoiTG9sS0xvOEJ1MlAyLUVHaTdBbHg6bDNVTFg5S2dUNmlmSXRzVXRyOTJDQSJ9

ℹ️  Configure other nodes to join this cluster:
• On this node:
  ⁃ Create an enrollment token with `bin/elasticsearch-create-enrollment-token -s node`.
  ⁃ Uncomment the transport.host setting at the end of config/elasticsearch.yml.
  ⁃ Restart Elasticsearch.
• On other nodes:
  ⁃ Start Elasticsearch with `bin/elasticsearch --enrollment-token <token>`, using the enrollment token that you generated.
━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
```
4. You can check if the elasticsearch instance is up and running on https://localhost:9200. The below statement should be printed. 
> [!IMPORTANT]
> Since Elasticsearch configures security automatically in the latest versions, please make sure to use HTTPS.

```JSON
{
  "name" : "MacBook-Pro.local",
  "cluster_name" : "elasticsearch",
  "cluster_uuid" : "E2qaGmBRQdSdXEp3ivHiBw",
  "version" : {
    "number" : "8.13.2",
    "build_flavor" : "default",
    "build_type" : "tar",
    "build_hash" : "16cc90cd2d08a3147ce02b07e50894bc060a4cbf",
    "build_date" : "2024-04-05T14:45:26.420424304Z",
    "build_snapshot" : false,
    "lucene_version" : "9.10.0",
    "minimum_wire_compatibility_version" : "7.17.0",
    "minimum_index_compatibility_version" : "7.0.0"
  },
  "tagline" : "You Know, for Search"
}
```
***

## 3.2 Configuring Kibana

1. Download Kibana from the official [Elastic website](https://www.elastic.co/downloads/kibana)
2. Navigate to **_/bin_** folder, and run the kibana instance using the below command.
```
./kibana
```
3. Then open the Kibana UI on http://localhost:5601. Paste the enrollment token printed in the elasticsearch logs. Use the username and password provided by elasticsearch. This automatically establishes a connection between the Elastisearch and Kibana instance.
4. Then navigate to the **Dev Tools** option under Management tab on the Kibana UI to interact with elasticsearch.
5. Type the below command in the console to fetch the health of the Elasticsearch instance.
```
GET /_cluster/health
```
It should print a result something like this
```JSON
{
  "cluster_name": "elasticsearch",
  "status": "yellow",
  "timed_out": false,
  "number_of_nodes": 1,
  "number_of_data_nodes": 1,
  "active_primary_shards": 39,
  "active_shards": 39,
  "relocating_shards": 0,
  "initializing_shards": 0,
  "unassigned_shards": 1,
  "delayed_unassigned_shards": 0,
  "number_of_pending_tasks": 0,
  "number_of_in_flight_fetch": 0,
  "task_max_waiting_in_queue_millis": 0,
  "active_shards_percent_as_number": 97.5
}
```
6. Then, create an index using the below command. You can replace the <<index-name>> with your index name, I have used 'netflix' as the index name.
```
PUT /<<index-name>>
```

***

## 3.3 Configuring Logstash

1. Download Logstash from the official [Elastic website](https://www.elastic.co/downloads/logstash)
2. Also, download the MySQL Connector/J from the official [MySQL website](https://dev.mysql.com/downloads/connector/j/). This will be used to form a connection between MySQL instance and Logstash.
3. Then, navigate to the **_/config_** folder and edit the **_logstash-sample.conf_** file. This file will contain the logic to scrape the data from MySQL database and insert it into the Elasticsearch instance. The file will contain the following data, and it can be changed according to your needs.
  - The 'input' section of the configuration deals with connecting with MySQL and running the query based on **_modified_timestamp_** column. If there are any rows created  after the last sync between Logstash and MySQL, they will be scraped.
  - The 'filter' section of the configuration deals with filtering any rows or transforming the data according to your needs.
  - The 'output' section of the configuration deals with outputting the scraped data into the console and the elasticsearch instance in the given index. 
```
input {
  jdbc {
    jdbc_driver_library => "/Users/abhijeet/eclipse-workspace/tools/mysql-connector-j-8.4.0/mysql-connector-j-8.4.0.jar" 
    jdbc_driver_class => "com.mysql.jdbc.Driver"
    jdbc_connection_string => "jdbc:mysql://127.0.0.1:3306/netflix" 
    jdbc_user => "root" 
    jdbc_password => "password" 
    jdbc_paging_enabled => true
    tracking_column => "unix_ts_in_secs"
    use_column_value => true
    tracking_column_type => "numeric"
    schedule => "0 */1 * * * *"
    statement => "SELECT *, UNIX_TIMESTAMP(modified_timestamp) AS unix_ts_in_secs FROM netflix_data WHERE (UNIX_TIMESTAMP(modified_timestamp) > :sql_last_value AND modified_timestamp < NOW()) ORDER BY modified_timestamp ASC"
  }
}
filter {
  mutate {
    copy => { "id" => "[@metadata][_id]"}
    remove_field => ["id", "@version", "unix_ts_in_secs"]
  }
}
output {
  stdout { codec =>  "rubydebug"}
  elasticsearch {
    hosts => ["https://localhost:9200"]
    user => "elastic"
    password => "I2VSOAmx4Xu-bRR5ZqmG"
    index => "netflix"
    ssl_certificate_verification => false
  }
}
```
4. Once you have done all the configuration, navigate to **_/bin_** folder and run the Logstash instance using the below command.
```
./logstash -f logstash-sample.conf
```
***
***

# 4. Spring Boot Application

## 4.1 Configuring Elasticsearch in Spring Boot

### 4.1.1 Elasticsearch Configuration

We need to add a configuration file which connects the Spring Boot application to the Elasticsearch instance. The below file contains the configuration:

```Java
@Configuration
@EnableElasticsearchRepositories(basePackages = "com.custom.search.es.repository")
public class ElasticsearchConfig extends ElasticsearchConfiguration {

	@Value("${spring.elasticsearch.host}")
	String elasticHost;

	@Value("${spring.elasticsearch.ca.fingerprint}")
	String elasticCaFingerprint;

	@Value("${spring.elasticsearch.username}")
	String elasticUsername;

	@Value("${spring.elasticsearch.password}")
	String elasticPassword;

	@Override
	public ClientConfiguration clientConfiguration() {
		return ClientConfiguration.builder().connectedTo(elasticHost).usingSsl(elasticCaFingerprint)
				.withBasicAuth(elasticUsername, elasticPassword).build();
	}

}
```
The properties can be kept in the application.properties file of the Spring Boot application. The SHA-256 fingerprint of the elastiscsearch instance certificate should be used here which was generated when the elasticsearch instance was first [started](#configuring-elasticsearch). Same with the username and password. The properties are defined below:

```
spring.elasticsearch.host=127.0.0.1:9200
spring.elasticsearch.ca.fingerprint=f9f3cc23489d84642a1f129703ebbb2cabfa918eb56da70a1d9cd67a534f75d1
spring.elasticsearch.username=elastic
spring.elasticsearch.password=I2VSOAmx4Xu-bRR5ZqmG
```
> [!IMPORTANT]
> Use the IP address of the elasticsearch instance when defining the host since using 'localhost' does not work here.


### 4.1.2 Elasticsearch Index Entity

An index in elasticsearch instance can be defined as a Java DTO object to perform operations using the Spring Boot application. The entity is defined below.

```Java
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Document(indexName = "netflix", createIndex = false)
public class NetflixData implements Serializable {

	private static final long serialVersionUID = 1063565029021759659L;

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
```
### 4.1.3 Elasticsearch Repository

The elasticsearch repository can be used to perform various simple search and save operations on the index. The repository can be defined as below:

```Java
@Repository
public interface NetflixDataRepository extends ElasticsearchRepository<NetflixData, String> {
	Page<NetflixData> findByTitleContainingIgnoreCase(String title, Pageable pageable);
}
```

### 4.1.4 ElasticsearchOperations 

For complex search queries on the elasticsearch index we can also use the ElasticsearchOperations class. The queries can be written as below:

```Java
@Repository
public class ElasticsearchDaoImpl implements ElasticsearchDao {

	private final ElasticsearchOperations elasticsearchOperations;

	public ElasticsearchDaoImpl(ElasticsearchOperations elasticsearchOperations) {
		this.elasticsearchOperations = elasticsearchOperations;
	}

	@Override
	public SearchHits<NetflixData> searchByType(String type, int page, int size) {
		Criteria criteria = new Criteria("type").is(type);

		Query query = new CriteriaQuery(criteria, PageRequest.of(page, size));

		return elasticsearchOperations.search(query, NetflixData.class);
	}

}
```

## 4.2 Configuring JUnit5, Mockito & JaCoCo

### 4.2.1 JUnit5 & Mockito

This Spring Boot application uses the latest JUnit 5 for writing test cases to test basic functionality of the APIs and business logic. 

1. Add JUnit5 and Mockito dependencies in **_pom.xml_** file.
```XML
<!-- https://mvnrepository.com/artifact/org.junit.jupiter/junit-jupiter-api -->
<dependency>
	<groupId>org.junit.jupiter</groupId>
	<artifactId>junit-jupiter-api</artifactId>
	<scope>test</scope>
</dependency>

<!-- https://mvnrepository.com/artifact/org.mockito/mockito-core -->
<dependency>
	<groupId>org.mockito</groupId>
	<artifactId>mockito-core</artifactId>
	<scope>test</scope>
</dependency>
```

2. Then simply add test cases in the **_/src/test/java_** package of the application.
```Java
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
}
```

### 4.2.2 Configuring JaCoCo

Code coverage is a software metric used to measure how many lines of our code are executed during automated tests. JaCoCo is a code coverage reports generator for Java projects.

1. Add JaCoCo Maven plugin in the **_pom.xml_** file of the project. You can customize this configuration according to your project. I have also excluded some of the packages which do not require test cases such as constants, entity classes and DTO classes.
```XML
<build>
	<pluginManagement>
		<plugins>
			<plugin>
				<groupId>org.jacoco</groupId>
				<artifactId>jacoco-maven-plugin</artifactId>
				<version>0.8.12</version>
			</plugin>
		</plugins>
	</pluginManagement>
	<plugins>
		<plugin>
			<groupId>org.jacoco</groupId>
			<artifactId>jacoco-maven-plugin</artifactId>
			<configuration>
				<excludes>
					<exclude>**/*com/custom/search/constants*/**</exclude>
					<exclude>**/*com/custom/search/es/entity*/**</exclude>
					<exclude>**/*com/custom/search/response*/**</exclude>
				</excludes>
			</configuration>
			<executions>
				<execution>
					<goals>
						<goal>prepare-agent</goal>
					</goals>
				</execution>
				<execution>
					<id>report</id>
					<phase>test</phase>
					<goals>
						<goal>report</goal>
					</goals>
				</execution>
			</executions>
		</plugin>
	</plugins>
</build>
```

2. Then you can run the Maven Test command for the project which automatically generates the test coverage report.
3. Navigate to the <strong><em><project_base_directory>/target/site/jacoco/index.html</em></strong>, and open the HTML file on web browser to view the coverage report. It should look something like the image below.

![JaCoCo Coverage Report](/img/jacoco.png)

## 4.3 Configuring SonarQube

We can also integrate SonarQube into our project to see an overview of all the code issues and test coverage reports in a singular dashboard. I have used Docker to create an instance of Sonarqube, since it is easy to manage. The steps to integrate sonarqube are given below:

1. Download the latest version of Sonarqube from Docker Dekstop.
2. Add the below lines in **_pom.xml_** file of your Spring Boot project. It integrates the sonar scanner maven plugin.
```XML
<build>
	<pluginManagement>
		<plugins>
			<plugin>
				<groupId>org.sonarsource.scanner.maven</groupId>
				<artifactId>sonar-maven-plugin</artifactId>
				<version>3.11.0.3922</version>
			</plugin>
		</plugins>
	</pluginManagement>
</build>
```
3. Once this is added you can login into the Sonarqube UI using the address http://localhost:9000
4. On the webpage, you can create a global token and give permissions to all projects for the same token.
5. Then, you can create a project with the project key and name according to your Spring Boot project. I have used 'SearchApplication' as project name and key.
6. Once the project is created, you can navigate to the project base directory in terminal and run the below command. This will send all the relevant code and JaCoCo reports to Sonarqube.
```
mvn clean verify sonar:sonar -Dsonar.projectKey=SearchApplication -Dsonar.projectName='SearchApplication' -Dsonar.host.url=http://localhost:9000 -Dsonar.token=sqa_5a29e458e7156bb2c95d8c8d06ea52cf926ba5c1
```
7. Then check the Sonarqube webpage to view the code issues and test coverage reports all in one place. It should look something like the below picture.

