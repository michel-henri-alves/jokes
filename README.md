# TAKE A JOKE REST API 

> Status: Finished 😃

### This project consumes  https://v2.jokeapi.dev/ and provides theirs informations in services.

## Endpoints available

+ GET /service/joke - Take a joke
+ GET /service/joke/{category} - Take a joke by category (Christmas, Pun, Spooky, Any, Programming, Misc, Dark)
+ POST /service/rate/{id}/{grade} - You can rate each joke defining a grade for the joke (using id to indicate joke)
+ GET /service/categories - Categories list sorted by rate
+ GET /service/unrated - List all consumed jokes without rate register

## Ways to run 

### install and tests
> run <mvn clean install> command by terminal

### execute 
> after install process java -jar jokes.jar inside folder target. Service will be available in http://localhost:8080

### inside docker 
> execute command <docker build -t jokes-docker .> inside jokes-docker project
> start with command <docker run --name jokes-docker -p 0.0.0.0:8080:8080/tcp jokes-docker> . Service will be available in http://localhost:8080

### Swaggers doc 
> available on http://localhost:8080/swagger-ui.html
