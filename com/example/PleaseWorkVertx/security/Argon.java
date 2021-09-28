/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.PleaseWorkVertx.security;

/**
 *
 * @author Charl3s
 */
import de.mkammerer.argon2.Argon2;
import de.mkammerer.argon2.Argon2Factory;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
public class Argon {
     public static void main(String[] args) {

        // default argon2i, salt 16 bytes, hash length 32 bytes.
        Argon2 argon2 = Argon2Factory.create();

        char[] password = "Hello World".toCharArray();

        Instant start = Instant.now();  // start timer

        try {
            // iterations = 10
            // memory = 64m
            // parallelism = 1
            String hash = argon2.hash(22, 65536, 1, password);
            System.out.println(hash);

            // argon2 verify hash
            /*if (argon2.verify(hash, password)) {
                System.out.println("Hash matches password.");
            }*/

            //int iterations = Argon2Helper.findIterations(argon2, 1000, 65536, 1);
            //System.out.println(iterations);

        } finally {
            // Wipe confidential data
            argon2.wipeArray(password);
        }

        Instant end = Instant.now();    // end timer

        System.out.println(String.format(
                "Hashing took %s ms",
                ChronoUnit.MILLIS.between(start, end)
        ));

    }

}

