# FlightTrack

The FlightTrack application provides visibility into realtime flight metrics and locations provided by the OpenSky Network Live API. 

## Prerequisites

Docker, Java 11, SBT/SBT console

- FlightTrack includes a sample data set, however it will require internet access to regularly poll for new flight data
- The application will also require the following ports be available: 3000 (Grafana UI), 35432 (PostGres DB for direct client access)

## Installation 

FlightTrack uses the following applications to support it's operations:

- Grafana - data visualization application
- Flight-Aggregator - AkkaStream based ingestion application
- PostgreSQL Database - Data Storage

All applications are containerized and networked via Docker Compose.

To build and execute the project, the following steps are required:

1.Build the flight-aggregator project and generate the relevant docker images

```shell script
# If running on the command line
sbt clean compile package docker

# If running using sbt shell use the following
;clean ;compile ;package ;docker
```

2.Build and start up container via docker compose

```shell script
# From project root navigate to the docker folder
cd docker

# build docker compose specific image files/configuration
docker compose build

# startup docker instances for application. We include -d flag to allow to run in background (removing flag allows logs to be visible
docker compose up -d
```
3.In a browser navigate to the FlightTrack Dashboard running on port 3000 at [http://localhost:3000/d/_2UICND/flighttrack-0-2v?orgId=1](http://localhost:3000/d/_2UICND/flighttrack-0-2v?orgId=1). Read only access is granted and a login is not required.

4.Application can be shutdown using ```docker compose down``` (when running without -d flag Ctrl-C may be required first).

# Usage

FlightTrack by default is set to monitor flights within the state of Georgia and portions of South Carolina.  Upon startup, data is provided reflecting a period of time on **Wednesday May 4 2022**. However users may choose to adjust the query interval on the FlightTrack dashboard to pull the most recent available data. 
Data is polled based on whatever live information is available for the region, therefore certain dates further in the past may not be readily available.

Initial queries from 1 to 6 hours from the current time should be sufficient to retrieve data for the area.
