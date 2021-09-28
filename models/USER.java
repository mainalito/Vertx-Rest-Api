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
public class USER {
    private String name;
    private String password;

    public USER(String name, String password) {
        this.name = name;
        this.password = password;
    }
    
}
