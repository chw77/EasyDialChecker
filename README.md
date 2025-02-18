# EasyDial Checker Service
<!-- TOC -->
- [Summary](#summary)
- [Design](#design)
    + [API](#api)
    + [Validation](#validation)
- [Setup](#setup)
    + [Prerequisites](#prerequisites)
    + [Developer Setup](#developer-setup)
    + [Deployment](#deployment)
- [Implementation Details](#implementation-details)
    +[Persistence](#persistence)
    +[Logging](#logging)
    + [Configuration](#configuration)
- [Testing](#testing)
    + [Unit Testing](#unit-testing)
    + [Manual Testing](#manual-testing)
- [Keypad Adjacent Map Logic](#keypad-adjacent-map-logic)
    + [Keypad Layout](#keypad-layout)
    + [Adjacent Map](#adjacent-map)
- [Additional Notes](#additional-notes)
    + [Assumptions](#assumptions)
- [Owner](#owner)

## Summary
The EasyDial Checker Service is a [Spring Boot](https://spring.io/projects/spring-boot) 3.3.8 application which exposes a [RESTful Web Service](https://docs.oracle.com/javaee/6/tutorial/doc/gijqy.html) API to validate input text is Easy To Dial or not.

## Design

### API
EasyDial Checker Service API exposes a single API method:
- /easydial - accepts easydial request containing text and returns easydial response with status
- Successful requests returns results with HTTP 200 OK status code.
- Sample API URL for the localhost environment:
    - HTTP POST
        - http://localhost:8080/easydial
    - HTTP Headers
        - Content-Type application/json
    - Request Body:

      `{
      "text": "4587"
      }`

    - Successful Response:

      `{
      "status": "true"
      }`

### Validation
- API accepts requests containing with digits (0-9) and all types of whitespace (including spaces, tabs, and newlines)
- The validation logic validates the text values
- Unsuccessful requests returns HTTP 400 Bad Request status code
    - Invalid EasyDial Request:

      `{
      "text": "9168"
      }`

    - Failed response with null status:

      `{
      "status": null
      }`
- Validation logic is configurable and can update using application.properties file.

## Setup

### Prerequisites
- Java 1.17 +

### Developer Setup
- Checkout the EasyDial Checker Service GitHub repository
> git clone [git@github.com:chw77/EasyDialChecker.git](https://github.com/chw77/EasyDialChecker)

- Build using maven
> mvn clean install
### Deployment
- Run the generated EasyDialChecker-1.0.0-SNAPSHOT.jar file under target folder
> java -jar EasyDialChecker-1.0.0-SNAPSHOT.jar

## Implementation Details
EasyDial Checker Service is implemented following the layered architecture.
- Controller
    - The main controller is:
        - EasyDialController.java
- Services
    - Underlying services are:
        - EasyDialService.java
        - CacheService.java
        - ValidationService.java
- Persistence
    - Persistence layer implementation is based on the
        - BaseRepository.java / FileRepository.java
- Utility
    - Utility service is:
        - CleanupUtils

### Persistence
- Valid input requests are stored under easydial-persistence.txt file. Filename can be configurable via application.properties file.

### Logging
- Logs are available under /log folder easydial-checker.log file. Log settings can be configurable via application.properties file.

### Configuration
Application supports config points via application.properties file.
- Validation Regex
- Persistence Filename
- Log Settings

## Testing

### Unit Testing
The EasyDial Checker Service Unit Tests follow the [Arrange-Act-Assert](http://wiki.c2.com/?ArrangeActAssert) test pattern and are performed using Spring and JUnit 5.
- Run unit tests using maven
> mvn clean test
### Manual Testing
Sample Postman requests are available under /postman folder for manual testing.

## Keypad Adjacent Map Logic
The **Adjacent Map** represents the mapping of adjacent keys on a standard telephone keypad. This logic is used to determine which numbers are adjacent to each other, allowing the validation of "easy-to-dial" phone numbers based on their position on the keypad.

### Keypad Layout
The standard keypad layout is represented as follows:

1  2  3  
4  5  6  
7  8  9<br>
\* 0  #




### Adjacent Map
The adjacent map is a pre-generated HashMap where each key (digit) maps to a set of adjacent digits.

| Key | Adjacent Keys            |
|-----|--------------------------|
| 0   | [7, 8, 9]                |
| 1   | [2, 4, 5]                |
| 2   | [1, 3, 4, 5, 6]          |
| 3   | [2, 6, 5]                |
| 4   | [1, 2, 5, 7, 8]          |
| 5   | [1, 2, 3, 4, 6, 7, 8, 9] |
| 6   | [2, 3, 5, 8, 9]          |
| 7   | [0, 4, 5, 8 ]            |
| 8   | [0, 4, 5, 6, 7, 9]       |
| 9   | [0, 5, 6, 8]             |

### Note:
- The adjacency map excludes the `*` and `#` since they are not considered in this mapping.

## Additional Notes
The main implementation decisions behind the EasyDial Checker Service are available here:

- Validate the  EasyDialRequest text.
- If the incoming request is valid:
    - Remove whitespaces (spaces, tabs, newlines) in the incoming text.
    - Check that the incoming text is available in the configured cache.
    - Cache values are populated based on the previously processed texts.
    - During the application startup read the persistence file (easydial-persistence.txt) to preload the cache.
    - During the application startup build the keypad adjacent map to check if the number is easy to use in the logic.
    - If the text is available in the cache:
        - Read the associated easy to dial status value from the cache and return EasyDialResponse.
        - If the text is available in the cache, persistence file won't be updated with the record to prevent the duplication.
    - If the text isn't available in the cache:
        - Perform EasyDial check logic to determine the incoming text status.
        - The **Easy to Dial** logic checks whether a phone number is easy to dial based on the adjacency of the digits on a standard telephone keypad. 
        - A phone number is considered **easy to dial** if each digit in the number is adjacent to the next digit on the keypad.
        - Update cache with the obtained results.
        - Persist the text and easy to dial status as a comma-delimited value in the persistence file.
    - Return successful EasyDialResponse with the obtained status.
- If the incoming request is invalid:
    - Return failure EasyDialResponse with the null status.

### Assumptions
- EasyDialResponse will return both success and failure scenarios with the different status.
    - success: **true** or **false**
    - failure: **null**
- Consecutive same digits (e.g. "111", "5566"") not being considered as "easy to dial."
- Descriptive exception details aren't exposed to the clients and log any encountered exceptions.
- Logs monitoring is required to identify the persistence layer data errors.

## Owner
Charitha Dunuwille
