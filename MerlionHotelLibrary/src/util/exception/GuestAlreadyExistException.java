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
public class GuestAlreadyExistException extends Exception {

    /**
     * Creates a new instance of <code>GuestAlreadyExistException</code> without
     * detail message.
     */
    public GuestAlreadyExistException() {
    }

    /**
     * Constructs an instance of <code>GuestAlreadyExistException</code> with
     * the specified detail message.
     *
     * @param msg the detail message.
     */
    public GuestAlreadyExistException(String msg) {
        super(msg);
    }
}
