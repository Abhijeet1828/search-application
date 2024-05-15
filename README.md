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



## 3. Confguring ELK Stack

This section will talk about how to install and configure the ELK stack 

### 3.1 Configuring Elasticsearch


