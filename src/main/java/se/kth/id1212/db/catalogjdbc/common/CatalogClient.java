/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.kth.id1212.db.catalogjdbc.common;

import java.rmi.Remote;
import java.rmi.RemoteException;
/**
 *
 * RMI that server can call on the client
 */
public interface CatalogClient extends Remote{
    /**
     * The specified message is received by the client.
     *
     * @param msg The message that shall be received.
     */
    void RrecvMsg(String msg) throws RemoteException;
}
