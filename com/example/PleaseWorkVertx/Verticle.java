/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.PleaseWorkVertx;

import database.JDBC;
import io.vertx.core.Vertx;

/**
 *
 * @author Charl3s                                                  
 */
public class Verticle {
    public static void main(String[] args){
        Vertx vertx = Vertx.vertx();
//       vertx.deployVerticle(new MainVerticle());
       //vertx.deployVerticle(new BasicAuth());
       // vertx.deployVerticle(new UserAuth());
        vertx.deployVerticle(new JDBC());
       
    }
}
