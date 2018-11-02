/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util.exception;

/**
 *
 * @author samue
 */
public class RoomInventoryNotFound extends Exception {

    public RoomInventoryNotFound() {
    }

    public RoomInventoryNotFound(String message) {
        super(message);
    }
    
}
