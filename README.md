# Yanader's Records

## Description

Welcome to Yanader's Records. A web-based source of all your favourite records, accessible through our own custom API (CLI coming soon!)

## API
### Information
This API currently works without authentication (on trust).

### base uri

```http://localhost:8080/api/v1/recordstore```

The base endpoint for this service.

### Endpoints

#### GET ```/albums```


#### GET ```/instock```
Returns a list of in-stock albums
```json[
{
"albumId": 1,
"albumName": "Zuma",
"artistName": "Neil young",
"quantity": 2,
"priceInPounds": 15.0
},
{
"albumId": 2,
"albumName": "Close to the edge",
"artistName": "Yes",
"quantity": 3,
"priceInPounds": 17.0
}
]
```

#### GET ```/albums/{id}```


#### POST ```/albums/add```


#### PUT ```/albums/{id}```


#### DELETE ```/albums/{id}```


#### PATCH ```/albums/{id}```


#### GET ```/health```
Standard /health endpoint supplied through Spring Actuator via redirect


## Documentation

The codebase is split into a number of packages.

1. Repository
   - Includes separate repositories for Albums and Stock.
   - Implement CrudRepository to supply standard CRUD functionality.
2. Model
   - Includes entity models for Album and Stock and a number of DTO objects for data transfer to/from the database
3. Service
   - Business logic drawing on the repository layer and supplying the controller
4. Controller
   - Endpoint definition with custom exception handling to provide users with detailed responses

## Future Inclusions

1. Staff Picks!
   - After searching for an album ID that doesn't exist, the user will additionally be presented with a staff pick!
2. Command Line Interface
   - As an alternative to the API, customers will be able to interact with our database through the command line
3. Security!
   - User profiles will be used to separate customer endpoints from staff endpoints. As a customer you'll be able to query the database as much as you like but only staff will be able to update it.
4. Multiple Query Parameters