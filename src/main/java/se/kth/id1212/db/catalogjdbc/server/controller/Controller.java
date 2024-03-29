package se.kth.id1212.db.catalogjdbc.server.controller;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;
import se.kth.id1212.db.catalogjdbc.common.Catalog;
import se.kth.id1212.db.catalogjdbc.server.integration.CatalogDAO;
import se.kth.id1212.db.catalogjdbc.server.integration.CatalogDBException;
import se.kth.id1212.db.catalogjdbc.server.model.Account;
import se.kth.id1212.db.catalogjdbc.common.AccountDTO;
import se.kth.id1212.db.catalogjdbc.server.model.AccountException;
import se.kth.id1212.db.catalogjdbc.server.model.RejectedException;
import se.kth.id1212.db.catalogjdbc.common.Credentials;
import se.kth.id1212.db.catalogjdbc.common.CatalogClient;
import se.kth.id1212.db.catalogjdbc.server.model.Participant;
import se.kth.id1212.db.catalogjdbc.server.model.ParticipantManager;


/**
 * Implementations of the catalog's remote methods, this is the only server class that can be called
 * remotely
 */
public class Controller extends UnicastRemoteObject implements Catalog {
    private final CatalogDAO catalogDb;
    private final ParticipantManager participantManager = new ParticipantManager();
    
        @Override
    public int Rlogin(CatalogClient remoteNode, Credentials credentials) {
        int participantId = participantManager.createParticipant(remoteNode, credentials);
        return participantId;
    }

     @Override
    public String RgetUsername(int id) throws RemoteException {
       return participantManager.findParticipant(id).RgetUsername();
    }
    
     @Override
    public CatalogClient RgetRemoteNode(int id) throws RemoteException {
       return participantManager.findParticipant(id).RgetClientHandler(); //Remote node
    }
    
    
    @Override
    public int RgetID(String userName) throws RemoteException {
       return participantManager.findParticipantByUserName(userName);
    }
    
    @Override
    public void Rlogout(int id) {
        participantManager.findParticipant(id).Rlogout();
        participantManager.removeParticipant(id);
    }

    
    /*@Override
    public Participant RGetParticipant(int id) throws RemoteException {
    return participantManager.findParticipant(id);
    }*/

    public Controller(String datasource, String dbms) throws RemoteException, CatalogDBException {
        super();
        catalogDb = new CatalogDAO(dbms, datasource);
    }

    @Override
    public synchronized List<? extends AccountDTO> listAccounts() throws AccountException {
        try {
            return catalogDb.findAllAccounts();
        } catch (Exception e) {
            throw new AccountException("Unable to list accounts.", e);
        }
    }

    @Override
    public synchronized void loginAccount(String userName, String passWord) throws AccountException{
        //String acctloggedin= " Already logged in into account for: " + userName + " .";
        String failureMsg = "Could not login into " + userName;
        try {
            catalogDb.loginAccount(getAcc(userName), passWord);
        }catch (Exception e) {
            throw new AccountException(failureMsg, e);
        }
    //}
    }
    
        @Override
    public synchronized void logoutAccount(String userName, String passWord) throws AccountException{
       // String acctloggedout= " Already logged out from account: " + userName + " .";
        String failureMsg = "Could not log out from " + userName;
        try {
            catalogDb.logoutAccount(getAcc(userName));
        }catch (Exception e) {
            throw new AccountException(failureMsg, e);
        }
    }
    
    @Override
    public synchronized void addafil(String userName, String passWord, String filenum, String url, String filename, int size) throws AccountException {
        String acctExistsMsg = "Account for: " + userName + " already exists";
        String failureMsg = "The acc already exists: " + userName;
        try {
            /*if (catalogDb.findAccountByName(userName) != null) {
            throw new AccountException(acctExistsMsg);
            }*/
            catalogDb.addafil(new Account(userName, passWord, filenum, url, filename, size, catalogDb));
        } catch (Exception e) {
            throw new AccountException(failureMsg, e);
        }
    }
    
    @Override
    public synchronized void createAccount(String userName, String passWord, String filenum) throws AccountException {
        String acctExistsMsg = "Account for: " + userName + " already exists";
        String failureMsg = "The acc already exists: " + userName;
        try {
            /*if (catalogDb.findAccountByName(userName) != null) {
            throw new AccountException(acctExistsMsg);
            }*/
            catalogDb.createAccount(new Account(userName, passWord, filenum, catalogDb));
        } catch (Exception e) {
            throw new AccountException(failureMsg, e);
        }
    }
    
    
@Override
    public synchronized void deleteAccount(String userName, String passWord) throws AccountException{
        String failureMsg = "Could unregister " + userName;
        try {
            catalogDb.deleteAccount(getAcc(userName), passWord);
        }catch (Exception e) {
            throw new AccountException(failureMsg, e);
        }
    }
    
    
    @Override
    public synchronized AccountDTO getAccount(String filenum) throws AccountException {
        if (filenum == null) {
            return null;
        }

        try {
            return catalogDb.findAccountByName(filenum);
        } catch (Exception e) {
            throw new AccountException("Could not search for account.", e);
        }
    }
    
    @Override
    public synchronized AccountDTO getAcc(String userName) throws AccountException {
        if (userName == null) {
            return null;
        }

        try {
            return catalogDb.findAccountByNom(userName);
        } catch (Exception e) {
            throw new AccountException("Could not search for account.", e);
        }
    }


    @Override //this is actually used for UPDATING A FILE
    public synchronized void fileupdating(AccountDTO acctDTO, String filenum, String filename, String url, int size, int access, int read, int write) throws RejectedException, AccountException {
        Account acct = (Account) getAccount(acctDTO.getFileNum());
        acct.fileupdating(filenum,filename,url,size,access, read,write);
        /*acct.filenumAdd(filenum);
        acct.filenameAdd(filename);
        acct.urlAdd(url);
        acct.sizeAdd(size);
        acct.accessAdd(access);
        acct.readAdd(read);
        acct.writeAdd(write); */
    }
     @Override
    public synchronized void filedelete(AccountDTO acctDTO, String filenum) throws RejectedException, AccountException {
        Account acct = (Account) getAccount(acctDTO.getFileNum());
        acct.filedelete(filenum);
    }
}
