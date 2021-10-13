# Vertx-Rest-Api
Implementation of MSSQL-CLIENT-VERTX-API and JDBC-CLIENT FOR SQL SERVER DATABASE
# Requirements
  1.JAVA 11 and above
  2.Vertx should be added to your pom.xml
  3.Maven
# Run
```public class Verticle {
    public static void main(String[] args){
        Vertx vertx = Vertx.vertx();
//       vertx.deployVerticle(new MainVerticle());
       //vertx.deployVerticle(new BasicAuth());
       // vertx.deployVerticle(new UserAuth());
        vertx.deployVerticle(new JDBC());
       
    }```
    Run the file usng any editor
