/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.kth.id1212.db.catalogjdbc.server.model;
import java.rmi.RemoteException;
import se.kth.id1212.db.catalogjdbc.common.CatalogClient;
import se.kth.id1212.db.catalogjdbc.common.MessageException;
/**
 *
 * @author Relax2954
 */
/**
 * Represents someone participating in catalog
 */
public class Participant {
    private static final String USERNAME_DELIMETER = ": ";
    private static final String DEFAULT_USERNAME = "anonymous";
    private final int id;
    private final CatalogClient remoteNode;
    private final ParticipantManager participantMgr;
    private String username;

    /**
     * Creates a new instance with the specified username and remote node.
     *
     * @param id         The unique identifier of this participant.
     * @param username   The username of the newly created instance.
     * @param remoteNode The remote endpoint of the newly created instance.
     * @param mgr        The only existing participant manager.
     */
    public Participant(int id, String username, CatalogClient remoteNode, ParticipantManager mgr) {
        this.id = id;
        this.username = username;
        this.remoteNode = remoteNode;
        this.participantMgr = mgr;
    }

    /**
     * Creates a new instance with the specified remote node and the default username.
     *
     * @param id         The unique identifier of this participant.
     * @param remoteNode The remote endpoint of the newly created instance.
     * @param mgr        The only existing participant manager.
     */
    public Participant(int id, CatalogClient remoteNode, ParticipantManager mgr) {
        this(id, DEFAULT_USERNAME, remoteNode, mgr);
    }



    

    /**
     * Checks if the specified remote node is the remote endpoint of this participant.
     *
     * @param remoteNode The searched remote node.
     * @return <code>true</code> if the specified remote node is the remote endpoint of this
     *         participant, <code>false</code> if it is not.
     */
    public boolean hasRemoteNode(CatalogClient remoteNode) {
        return remoteNode.equals(this.remoteNode);
    }

    /**
     * @param username The new username of this participant.
     */
    public void changeUsername(String username) {
        this.username = username;
    }
    public String RgetUsername(){
    return this.username;
    }
    public CatalogClient RgetClientHandler()
    {
        return this.remoteNode;
    }
    
    

    /**
     * Participant leaving
     */
    public void Rlogout() {
    }

}