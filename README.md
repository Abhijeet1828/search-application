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

## 3. Confguring ELK Stack

This section will talk about how to install and configure the ELK stack. There are two main things, how to connect Elasticsearch and Kibana, configuring Logstash to periodically scrape data from MySQL and insert into Elasticsearch.

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

