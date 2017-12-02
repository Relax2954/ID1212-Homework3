/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.kth.id1212.db.catalogjdbc.common;

/**
 *
 * @author Relax2954
 */
/**
 * Thrown when the expected message could not be received.
 */
public class MessageException extends RuntimeException {
    public MessageException(String msg) {
        super(msg);
    }
    
    public MessageException(Throwable rootCause) {
        super(rootCause);
    }
}