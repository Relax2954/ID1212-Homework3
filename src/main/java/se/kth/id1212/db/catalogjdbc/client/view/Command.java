
package se.kth.id1212.db.catalogjdbc.client.view;

/**
 * Defines all commands that can be performed by a user of the chat application.
 */
public enum Command {
    /**
     * Creates a new account.
     */
    REGISTER,
    //Logins into an account
    LOGIN,
    //Logouts from an account
    LOGOUT,
    /**
     * Lists all existing accounts.
     */
    LIST,
    /**
     * Deletes the specified account.
     */
    UNREGISTER,
    
    //ADDS A SPECIFIC FILE
    UPLOADFILE,
    //DOWNLOADS A FILE
    DOWNLOADFILE,
    /**
     * UPDATES the specified file
     */
    UPDATEFILE,
    /**
     * Deletes the specified file from the specified account
     */
    DELETEFILE,
    /**
     * Reads a specific file.
     */
    FILEREAD,
    /**
     * Lists all commands.
     */
    HELP,
    /**
     * Leave the chat application.
     */
    QUIT,
    /**
     * None of the valid commands above was specified.
     */
    ILLEGAL_COMMAND
}
