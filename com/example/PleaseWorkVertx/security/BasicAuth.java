/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.PleaseWorkVertx.security;
import io.vertx.core.Promise;
import io.vertx.ext.auth.*;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.auth.authentication.AuthenticationProvider;
import io.vertx.ext.auth.authorization.AuthorizationProvider;
import io.vertx.ext.auth.authorization.PermissionBasedAuthorization;
import io.vertx.ext.auth.authorization.RoleBasedAuthorization;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;

/**
 * @author <a href="http://tfox.org">Tim Fox</a>
 */


public class BasicAuth extends AbstractVerticle {

    AuthenticationProvider authProvider;
    Router router = Router.router(vertx);

    @Override
    public void start(Promise<Void> startPromise) throws Exception {

        router.get("/api/*").handler(this::example1);
        vertx.createHttpServer().requestHandler(router::accept).listen(
                //retrieve port from the configuration file
                config().getInteger("http.port", 8888), http -> {
            if (http.succeeded()) {
                startPromise.complete();
                System.out.println("HTTP server started on port 8888");
            } else {
                startPromise.fail(http.cause());
            }
        });
    }

    public void example1(RoutingContext ctx) {

        JsonObject authInfo = new JsonObject()
                .put("username", "tim").put("password", "mypassword");

        authProvider.authenticate(authInfo)
                .onSuccess(user -> {
                    System.out.println("User " + user.principal() + " is now authenticated");
                    ctx.response().setStatusCode(200).putHeader("content-type", "application/json; charset=utf-8")
                .setChunked(true).end(new JsonObject().put("USER", user.principal()).encodePrettily());
                })
//                .onFailure(Throwable::printStackTrace
                .onFailure(failure -> {
                    System.out.println("failed" + failure);
                    ctx.response().setStatusCode(500);
                }
        );
         
    }

    public void example2(User user, AuthorizationProvider authorizationProvider) {
        // load the authorization for the given user:
        authorizationProvider.getAuthorizations(user)
                .onSuccess(done -> {
                    // cache is populated, perform query
                    if (PermissionBasedAuthorization.create("printer1234").match(user)) {
                        System.out.println("User has the authority");
                    } else {
                        System.out.println("User does not have the authority");
                    }
                });
    }

    public void example3(User user, AuthorizationProvider authorizationProvider) {
        // load the authorization for the given user:
        authorizationProvider.getAuthorizations(user)
                .onSuccess(done -> {
                    // cache is populated, perform query
                    if (RoleBasedAuthorization.create("admin").match(user)) {
                        System.out.println("User has the authority");
                    } else {
                        System.out.println("User does not have the authority");
                    }
                });
    }

    public void example4(Vertx vertx) {
        // Generate a secure token of 32 bytes as a base64 string
        String token = VertxContextPRNG.current(vertx).nextString(32);
        // Generate a secure random integer
        int randomInt = VertxContextPRNG.current(vertx).nextInt();
    }

    public void example5() {
        KeyStoreOptions options = new KeyStoreOptions()
                .setPath("/path/to/keystore/file")
                .setType("pkcs8")
                .setPassword("keystore-password")
                .putPasswordProtection("key-alias", "alias-password");
    }

    public void example6(Vertx vertx) {
        PubSecKeyOptions options = new PubSecKeyOptions()
                .setAlgorithm("RS256")
                .setBuffer(
                        vertx.fileSystem()
                                .readFileBlocking("/path/to/pem/file")
                                .toString());
    }

    public void example7(Vertx vertx, AuthenticationProvider ldapAuthProvider, AuthenticationProvider propertiesAuthProvider) {
        // users will be checked on the 2 providers
        // and on the first success the operation completes
        ChainAuth.any()
                .add(ldapAuthProvider)
                .add(propertiesAuthProvider);
    }

    public void example8(Vertx vertx, AuthenticationProvider ldapAuthProvider, AuthenticationProvider propertiesAuthProvider) {
        // users will be checked on the 2 providers
        // and on all providers success the operation completes
        ChainAuth.all()
                .add(ldapAuthProvider)
                .add(propertiesAuthProvider);
    }

    public void example9(User user) {

        // check if user has a well known property
        if (user.containsKey("sub")) {
            // the check will first assert that the attributes contain
            // the given key and if not assert that the principal contains
            // the given key

            // just like the check before the get will follow the same
            // rules to retrieve the data, first "attributes" then "principal"
            String sub = user.get("sub");
        }
    }
}
