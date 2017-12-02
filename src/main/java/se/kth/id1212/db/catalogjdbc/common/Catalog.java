package se.kth.id1212.db.catalogjdbc.common;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;
import se.kth.id1212.db.catalogjdbc.server.model.AccountException;
import se.kth.id1212.db.catalogjdbc.server.model.Participant;
import se.kth.id1212.db.catalogjdbc.server.model.RejectedException;

/**
 * Specifies the catalog's remote methods.
 */
public interface Catalog extends Remote {
    /**
     * The default URI of the catalog server in the RMI registry.
     */
    public static final String CATALOG_NAME_IN_REGISTRY = "catalog";
    
    int Rlogin(CatalogClient remoteNode, Credentials credentials) throws RemoteException;
    
    void RchangeNickname(int id, String username) throws RemoteException;
    
    void RnotifyMsg(int id, String msg) throws RemoteException;
    public String RgetUsername(int id) throws RemoteException;
    public int RgetID(String userName) throws RemoteException;
    //public Participant RGetParticipant(int id) throws RemoteException;
    public CatalogClient RgetRemoteNode(int id)  throws RemoteException;
    
    void Rlogout(int id) throws RemoteException;

    

    
    /**
     * Creates an account with the specified name and the balance zero.
     *
     * @param name The account user's name.
     * @param password
     * @param filenum
     * @throws RemoteException  If unable to complete the RMI call.
     * @throws AccountException If unable to create the account.
     */
    public void createAccount(String name, String password, String filenum) throws RemoteException, AccountException;
    public void deleteAccount(String name, String password) throws RemoteException, AccountException;
    //adding a file
    public void addafil(String name, String password, String filenum, String url, String filename, int size ) throws RemoteException, AccountException;
    //logins into an already-existant account
    public void loginAccount(String name, String password) throws RemoteException, AccountException;
    //logs out from an account
     public void logoutAccount(String name, String password) throws RemoteException, AccountException;
    /**
     * getAccount Returns the account by SEARCHING THE FILENUM OF IT  !!!!!!!
     * @param filenum
     * @return 
     * @throws RemoteException  If unable to complete the RMI call.
     * @throws AccountException If unable to search for the account.
     */
    public AccountDTO getAccount(String filenum) throws RemoteException, AccountException;
    
    // * Returns the account by searching for the NAME OF THE ACC of it !!!!!!!
    public AccountDTO getAcc(String name) throws RemoteException, AccountException;
    

    /**
     * Lists all accounts in the catalog.
     * 
     * @return A list of all accounts.
     * @throws RemoteException  If unable to complete the RMI call.
     * @throws AccountException If unable to list accounts.
     */
    public List<? extends AccountDTO> listAccounts() throws RemoteException, AccountException;

    
    //FILEADDING IS USED FOR UPDATING A FILE
    public void fileupdating(AccountDTO acct, String filenum, String filename, String url, int size, int access, int read, int write) throws RemoteException, RejectedException,
                                                         AccountException;
    
     /**
     * Deletes the specified file from the specified account.
     *
     * @param acct   The account from which to delete a file.
     * @param filenum
     * @param fileName the name of the file in the database to delete
     * @throws RemoteException   If unable to complete the RMI call.
     * @throws RejectedException If the specified file is non-existant, or if unable to perform the update-
     * to delete the file.
     * @throws AccountException If unable to retrieve the file.
     */
    public void filedelete(AccountDTO acct, String filenum) throws RemoteException, RejectedException,
                                                          AccountException;
}
