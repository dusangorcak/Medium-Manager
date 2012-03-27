/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cds.storage;

/**
 *
 * @author Dusan
 */
public class RunTimeFailureException extends RuntimeException {
    
    public RunTimeFailureException(String msg) {
        super(msg);
    }

    public RunTimeFailureException(Throwable cause) {
        super(cause);
    }

    public RunTimeFailureException(String message, Throwable cause) {
        super(message, cause);
    }
}
