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
Returns a list of all albums on record.
```json
[
    {
        "albumId": 1,
        "albumName": "Zuma",
        "artistName": "Neil Young",
        "releaseDate": "1975-11-10",
        "genre": "ROCK"
    },
...
    {
        "albumId": 5,
        "albumName": "Artpop",
        "artistName": "Lady Gaga",
        "releaseDate": "2013-11-06",
        "genre": "POP"
    }
]
```
##### Query Parameters

The API currently only takes one query parameter at a time.

| Name | Description | Type |
|------|-------------|------|
| name | A list of albums with the specified name. | Text |
| artistname | A list of albums by the specified artist. | Text |
| year | A list of albums released in the given year | Number |
| genre | A list of albums of the given genre | Category (See below) |

| Genres |
|--------|
| ROCK |
| POP |
| COUNTRY |
| JAZZ |
| CLASSICAL |
| DANCE |
| SOUL |


#### GET ```/instock```
Returns a list of in-stock albums. 
```json
[
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
Returns album information based on its id or advises an album doesn't exist.
```json
{
    "albumId": 1,
    "albumName": "Zuma",
    "artistName": "Neil young",
    "quantity": 2,
    "priceInPounds": 15.0
}
```
```json
{
    "status": 404,
    "message": "No album with id 3 exists"
}
```

#### POST ```/albums/add```
Adds an album into the database. Responds with the same album details or an error message.

##### Body
```json
{
    "albumName": "oh how we drift away",
    "artistName": "tim heidecker",
    "priceInPence": 1199,
    "releaseDate": "2020-09-25",
    "genre": "COUNTRY"
}
```
##### Response
```json
{
    "albumId": 6,
    "albumName": "oh how we drift away",
    "artistName": "tim heidecker",
    "releaseDate": "2020-09-25",
    "genre": "COUNTRY"
}
```
##### Error
```json
{
    "status": 400,
    "message": "Missing Details: albumName, artistName, priceInPence, releaseDate(yyyy-mm-dd), genre(See documentation for list)"
}
```

#### PUT ```/albums/{id}```
Updates an existing album with id based on path variable. Requires full body in request

##### Body
```json
{
   "albumName": "Owls",
   "artistName": "Owls",
   "priceInPence": 1299,
   "releaseDate": "2001-07-31",
   "genre": "JAZZ"
}
```
##### Response
```json
{
   "albumId": 6,
   "albumName": "Owls",
   "artistName": "Owls",
   "quantity": 1,
   "priceInPounds": 12.99
}
```
##### Error
```json
{
    "status": 400,
    "message": "Missing Details: albumName, artistName, priceInPence, releaseDate(yyyy-mm-dd), genre(See documentation for list)"
}
```

#### DELETE ```/albums/{id}```
Deletes album based on path variable

##### Error
```json
{
   "status": 404,
   "message": "Deletion failed. No record at id 6"
}
```

#### PATCH ```/albums/{id}```
Update selected fields on the album id specified in path variable. This endpoint allows any combination of attributes to be included in the body.

##### Body
```json
{
   "priceInPence": 1799
}
```
##### Response
```json
{
   "albumId": 1,
   "albumName": "zuma",
   "artistName": "neil young",
   "releaseDate": "1975-11-10",
   "genre": "ROCK"
}
```
##### Error
```json
{
   "status": 404,
   "message": "No album with id 10 exists"
}
```

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
   - Note: The API often provides priceInPounds for improved customer experience but the Stock repository holds priceInPence as an integer

## Future Inclusions

1. Staff Picks!
   - After searching for an album ID that doesn't exist, the user will additionally be presented with a staff pick!
2. Command Line Interface
   - As an alternative to the API, customers will be able to interact with our database through the command line
3. Security!
   - User profiles will be used to separate customer endpoints from staff endpoints. As a customer you'll be able to query the database as much as you like but only staff will be able to update it.
4. Multiple Query Parameters