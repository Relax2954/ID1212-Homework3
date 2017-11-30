
package se.kth.id1212.db.catalogjdbc.client.view;

/**
 * Defines all commands that can be performed by a user of the chat application.
 */
public enum Command {
    /**
     * Creates a new account.
     */
    NEW,
    /**
     * Lists all existing accounts.
     */
    LIST,
    /**
     * Deletes the specified account.
     */
    DELETE,
    /**
     * Adds the specified file to the specified account
     */
    ADDFILE,
    /**
     * Deletes the specified file from the specified account
     */
    DELETEFILE,
    /**
     * Lists the files of the specified account.
     */
    FILELIST,
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
