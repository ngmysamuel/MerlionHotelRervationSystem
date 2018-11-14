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
public class RateNameNotUniqueException extends Exception {

    /**
     * Creates a new instance of <code>RateNameNotUniqueException</code> without
     * detail message.
     */
    public RateNameNotUniqueException() {
    }

    /**
     * Constructs an instance of <code>RateNameNotUniqueException</code> with
     * the specified detail message.
     *
     * @param msg the detail message.
     */
    public RateNameNotUniqueException(String msg) {
        super(msg);
    }
}
