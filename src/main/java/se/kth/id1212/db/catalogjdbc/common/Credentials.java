/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.kth.id1212.db.catalogjdbc.common;


import java.io.Serializable;

/**
 * Identifies a user.
 */
public class Credentials implements Serializable {
    private final String username;
    private final String password;

    /**
     * Creates a new instance with the specified username and password.
     */
    public Credentials(String username, String password) {
        this.username = username;
        this.password = password;
    }

    /**
     * Get the value of password
     *
     * @return the value of password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Get the value of username
     *
     * @return the value of username
     */
    public String getUsername() {
        return username;
    }
}
