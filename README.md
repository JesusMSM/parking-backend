# Backend for Toll Parking app

## Architecture
This project uses Spring, Hibernate, PostgreSQL, Java and Docker.
This project represents the backend part. 
The Model-Repository-Service-Controller design pattern has been followed. There are two entities, Parking and Slot. Slot has a foreign key pointing to a previously created Parking.
All the CRUD operations can be accesed by their controllers and their routes have been exposed.

## How to run it

Clone the repository and execute in the root folder:
```
docker-compose build
docker-compose up
```

Once it has been launched, it will be listening requests at the port 8080.
Swagger has been configured to provide documentation on the APIs. 
It can be accessed through http://localhost:8080/swagger-ui.html#/ once the service is running.

## Output JSON structure

You can create a new Parking by making a HTTP POST request to http://localhost:8080/api/parkings/create with the following payload:

```
{
  "fixedPrice": 0,
  "id": 0,
  "pricePerHour": 0,
  "pricingPolicy": "DYNAMIC",
  "slots": [
    {
      "numberOfAvailableSlots": 0,
      "slotType": "GASOLINE"
    }
  ]
}
```
For the arrival of a car you can use a HTTP POST to http://localhost:8080/api/parkings/{parkingId}/carArrival with the following payload

```
{
  "carType": "GASOLINE",
  "plateNumber": "string"
}
```

For the departure of a car you can use a HTTP POST to http://localhost:8080/api/parkings/{parkingId}/carDeparture with the following payload

```
{
  "plateNumber": "string"
}
```

More HTTP request can be found in http://localhost:8080/swagger-ui.html#/ .
