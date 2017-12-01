package se.kth.id1212.db.catalogjdbc.common;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;
import se.kth.id1212.db.catalogjdbc.server.model.AccountException;
import se.kth.id1212.db.catalogjdbc.server.model.RejectedException;

/**
 * Specifies the catalog's remote methods.
 */
public interface Catalog extends Remote {
    /**
     * The default URI of the catalog server in the RMI registry.
     */
    public static final String CATALOG_NAME_IN_REGISTRY = "catalog";
    
    /**
     * Creates an account with the specified name and the balance zero.
     *
     * @param name The account user's name.
     * @param password
     * @throws RemoteException  If unable to complete the RMI call.
     * @throws AccountException If unable to create the account.
     */
    public void createAccount(String name, String password, int filenum) throws RemoteException, AccountException;

    //logins into an already-existant account
    public void loginAccount(String name, String password) throws RemoteException, AccountException;
    //logs out from an account
     public void logoutAccount(String name, String password) throws RemoteException, AccountException;
    /**
     * Returns the account of the specified user, or <code>null</code> if there is no such
     * account.
     *
     * @param userName The user whose account to search for.
     * @return The account of the specified user, or <code>null</code> if there is no such
     *         account.
     * @throws RemoteException  If unable to complete the RMI call.
     * @throws AccountException If unable to search for the account.
     */
    public AccountDTO getAccount(String name) throws RemoteException, AccountException;
    
    public  AccountDTO getAccountByFileName(String fileName)  throws RemoteException, AccountException;

    /**
     * Deletes the specified account, if there is such an account. If there is no
     * such account, nothing happens.
     *
     * @param account The account to delete.
     * @throws RemoteException  If unable to complete the RMI call.
     * @throws AccountException If unable to delete account, or unable to check if there was an
     *                          account to delete.
     */
    public void deleteAccount(AccountDTO account) throws RemoteException, AccountException;

    /**
     * Lists all accounts in the catalog.
     * 
     * @return A list of all accounts.
     * @throws RemoteException  If unable to complete the RMI call.
     * @throws AccountException If unable to list accounts.
     */
    public List<? extends AccountDTO> listAccounts() throws RemoteException, AccountException;

    public void fileadding(AccountDTO acct, int filenum, String filename, String url, int size, int access, int read, int write) throws RemoteException, RejectedException,
                                                         AccountException;
    
     /**
     * Deletes the specified file from the specified account.
     *
     * @param acct   The account from which to delete a file.
     * @param fileName the name of the file in the database to delete
     * @throws RemoteException   If unable to complete the RMI call.
     * @throws RejectedException If the specified file is non-existant, or if unable to perform the update-
     * to delete the file.
     * @throws AccountException If unable to retrieve the file.
     */
    public void filedelete(AccountDTO acct, int filenum) throws RemoteException, RejectedException,
                                                          AccountException;
}
