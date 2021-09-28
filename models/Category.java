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
public class Category {
    private String category_id;
    private String category_name;

    public Category(String category_id, String category_name) {
        this.category_id = category_id;
        this.category_name = category_name;
    }
    
}
