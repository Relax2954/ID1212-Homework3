/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.kth.id1212.db.catalogjdbc.server.model;
import se.kth.id1212.db.catalogjdbc.common.CatalogClient;
import se.kth.id1212.db.catalogjdbc.common.Credentials;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * Keeps track of all active participants in the catalog, and is also responsible for sending
 * messages to participants.
 */
public class ParticipantManager {
    private final Random idGenerator = new Random();
    private final Map<Integer, Participant> participants = Collections.synchronizedMap(new HashMap<>());
    private final Map<String, Integer> easyUsernameFinder = Collections.synchronizedMap(new HashMap<>());

    public int createParticipant(CatalogClient remoteNode, Credentials credentials) {
        int participantId = idGenerator.nextInt();
        Participant newParticipant = new Participant(participantId, credentials.getUsername(),
                                                     remoteNode, this);
        participants.put(participantId, newParticipant);
        easyUsernameFinder.put(credentials.getUsername(), participantId);
        return participantId;
    }



    /**
     * Searches for a participant with the specified id.
     *
     * @param id The id of the searched participant.
     * @return The participant with the specified id, or <code>null</code> if there is no such
     *         participant.
     */
    public Participant findParticipant(int id) {
        return participants.get(id);
    }
    
    public int findParticipantByUserName(String userName) {
        return easyUsernameFinder.get(userName);
    }

    /**
     * Removes the specified participant from the catalog. No more messages will be sent to
     * that participant.
     *
     * @param id The id of the participant that shall be removed.
     */
    public void removeParticipant(int id) {
        participants.remove(id);
    }


}