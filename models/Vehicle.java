/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 *
 * @author Charl3s
 */
@Getter
@Setter
@NoArgsConstructor
public class Vehicle {
   private String id;
   private String name;
   private String brand;

    public Vehicle(String id, String name, String brand) {
        this.id = id;
        this.name = name;
        this.brand = brand;
    }
   
}
