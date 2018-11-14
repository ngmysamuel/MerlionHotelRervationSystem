/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util.exception;

/**
 *
 * @author Asus
 */
public class NoAvailableRoomsException extends Exception {

    /**
     * Creates a new instance of <code>NoAvailableRoomsException</code> without
     * detail message.
     */
    public NoAvailableRoomsException() {
    }

    /**
     * Constructs an instance of <code>NoAvailableRoomsException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public NoAvailableRoomsException(String msg) {
        super(msg);
    }
}
