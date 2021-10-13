# Vertx-Rest-Api
* Implementation of MSSQL-CLIENT-VERTX-API and JDBC-CLIENT FOR SQL SERVER DATABASE
## JDBC-CLIENT FOR SQL SERVER DB CONNECTION
```java
   final JDBCClient client = JDBCClient.createShared(vertx, new JsonObject()
                .put("url", "jdbc:sqlserver://localhost:1433;databaseName=master")
                .put("driver_class", "com.microsoft.sqlserver.jdbc.SQLServerDriver")
                .put("max_pool_size", 30)
                .put("user", "sa")
                .put("password", "123456789"));
        JDBCAuth authProvider = JDBCAuth.create(vertx, client);
```
## MSSQL-CLIENT-VERTX-API DATABASE CONNECTION
```java
 MSSQLConnectOptions connectOptions = new MSSQLConnectOptions()
            .setPort(1433)
            .setHost("localhost")
            .setDatabase("Ebook")
            .setUser("sa")
            .setPassword("123456789");
    //pool options
    PoolOptions poolOptions = new PoolOptions()
            .setMaxSize(100);

    //client pool
    MSSQLPool client = MSSQLPool.pool(vertx, connectOptions, poolOptions);
    
```
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

 
