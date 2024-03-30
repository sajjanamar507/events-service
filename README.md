# Event API

This service contains REST API for managing data about events, venues, and artists.
This Service provides an endpoint for retrieving all the events the artist will perform at.

## Endpoints

The following endpoints are available:

- `GET /api/v1/artists/{artistId}`: Based on the artistID, This end point retrieves all the events the artist will perform at .

**Path Parameters**
- `artistId` (required) - The ID of the artist.

## Error Handling

The API handles three types of exceptions and provides Json response based on the exception

- **Invalid Artist ID**: If an invalid artist ID is provided (e.g. If the ID is empty or null), the API will return a 500 Internal Server Error response.

- **Web Client Error**: If there is an error while fetching data from any of the upstream API's, the API will return a 500 Internal Server Error response.

- **Artist NotFound Exception**: If the given artist ID is not present in the  upstream API,the API will return a 404 NOT FOUND Error response.

## Technologies Used

This API was built using the following technologies:

- Java 21
- Spring Boot
- Reactive programming
- WebClient
- Lombok
- Reactor Test
- JUnit

## Setup

To run the Event service locally, follow these steps:

1. Clone this repository to your local machine.
2. Open the project in your IDE of choice.
3. Build the project using Maven.
4. Set the necessary environment variables:

    - `base.url`: the base URL for the external API (e.g. https://iccp-interview-data.s3-eu-west-1.amazonaws.com/78656681)
    - `artist.uri`: /artists.json
    - `event.uri`:  /events.json
    - `venue.uri` : /venues.json

## Installation

To build and run the Event API, follow these steps:

1. Clone the git repository: `git clone https://github.com/{YOUR_USERNAME}/event-service.git`
2. Navigate to the project directory: `cd event-service`
3. Build the project with Maven: `./mvn clean install`
4. Run the project: `./mvn spring-boot:run`

## Trade-offs and Design Decisions

 Trade-offs and design decisions made in the implementation of this API include:

- **Use of Spring Boot**: Spring Boot's is used as it support for reactive programming and allows to handle asynchronous and non-blocking operations efficiently.
- **Use of S3 as a REST Endpoint**: S3 was used as a REST endpoint for fetching the data, Accessing data stored in S3 is not a good approach. As this is an assignment it can be used keep things simple, but in a real-world scenario a more appropriate data store or API may be used.
- **Use of WebClient**: The utilization of WebClient for HTTP requests to the upstream API was chosen due to its reactive and non-blocking nature, which seamlessly integrates with Spring
- **Use of  Reactor Test and JUnit**: Reactor Test and JUnit were used for thorough testing purposes.
- **Use of  Lombok Test and SLF4J logging**: invaluable in minimizing boilerplate code within model classes and SLF4J logging was used for a flexible and standardized logging framework