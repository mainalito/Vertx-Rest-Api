/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package database;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.auth.User;
import io.vertx.ext.auth.VertxContextPRNG;
import io.vertx.ext.auth.jdbc.JDBCAuth;
import io.vertx.ext.auth.jdbc.JDBCAuthentication;
import io.vertx.ext.auth.jdbc.JDBCAuthenticationOptions;
import io.vertx.ext.jdbc.JDBCClient;
import io.vertx.ext.sql.ResultSet;
import io.vertx.ext.sql.SQLConnection;
import java.util.List;

/*
 * @author <a href="mailto:pmlopes@gmail.com">Paulo Lopes</a>
 */
public class JDBC extends AbstractVerticle {

    final static String FETCHALL = "select * from Categories";

    @Override

    public void start() throws Exception {

        final JDBCClient client = JDBCClient.createShared(vertx, new JsonObject()
                .put("url", "jdbc:sqlserver://localhost:1433;databaseName=master")
                .put("driver_class", "com.microsoft.sqlserver.jdbc.SQLServerDriver")
                .put("max_pool_size", 30)
                .put("user", "sa")
                .put("password", "123456789"));
        JDBCAuth authProvider = JDBCAuth.create(vertx, client);
//        
//        JDBCAuthenticationOptions options = new JDBCAuthenticationOptions();
//        JDBCAuthentication authenticationProvider = JDBCAuthentication.create(client, options);
//        String hash = authenticationProvider.hash("pbkdf2", //hashing algorithm
//                VertxContextPRNG.current().nextString(32),//secure random salt
//                "sausage" //password
//        );
        String salt = authProvider.generateSalt();
        String hash = authProvider.computeHash("sausage", salt);
        String hash1 = authProvider.computeHash("charles", salt);
        authProvider.setNonces(new JsonArray().add("random_hash_1").add("random_hash_1"));

        String salt1 = authProvider.generateSalt();
// we will pick the second nonce
        String hash2 = authProvider.computeHash("sausages", salt1, 1);
        JsonObject authInfo = new JsonObject().put("username", "chalo")
                .put("password", "sausages");
        authProvider.authenticate(authInfo, res -> {
            if(res.succeeded()){
                System.out.println("works");
                User user = res.result();
                System.out.println(user);
            }
            else{
                System.out.println("failed");
            }
        });
        System.out.println("hash " + hash);
        System.out.println("hash " + hash);

        System.out.println("hash1 " + hash1);
        String name = "chalito";
        client.getConnection(conn -> {
            if (conn.failed()) {
                System.err.println(conn.cause().getMessage());
                return;
            }

            final SQLConnection connection = conn.result();

            // insert some test data
            connection.updateWithParams("insert into users values(?, ?)", new JsonArray().add(name).add(salt), insert -> {;
                // query some data
                if (insert.succeeded()) {
                    connection.query("select * from users", rs -> {
                        rs.result().getResults().forEach(line -> {
                            System.out.println(line.encode());
                        });

                        // and close the connection
                        connection.close(done -> {
                            if (done.failed()) {
                                throw new RuntimeException(done.cause());
                            }
                        });

                    });
                } else {
                    System.out.println(insert.failed());
                }
            });
        });
    }
}
