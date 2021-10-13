# Vertx-Rest-Api
* Implementation of MSSQL-CLIENT-VERTX-API and JDBC-CLIENT FOR SQL SERVER DATABASE
* Provides understanding on CRUD APIs and Database connection

# Requirements
  * JAVA 11 and above
  * Vertx should be added to your pom.xml
  * Maven
# Run 
```java
public class Verticle {
    public static void main(String[] args){
        Vertx vertx = Vertx.vertx();
//       vertx.deployVerticle(new MainVerticle());
       //vertx.deployVerticle(new BasicAuth());
       // vertx.deployVerticle(new UserAuth());
        vertx.deployVerticle(new JDBC());
       
    }
```
Run the file on any java editor
# TESTS
The tests are done on POSTMAN

 
