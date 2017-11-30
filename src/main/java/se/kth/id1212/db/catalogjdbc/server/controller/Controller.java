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

/**
 * Implementations of the catalog's remote methods, this is the only server class that can be called
 * remotely
 */
public class Controller extends UnicastRemoteObject implements Catalog {
    private final CatalogDAO catalogDb;

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
    public synchronized void createAccount(String userName) throws AccountException {
        String acctExistsMsg = "Account for: " + userName + " already exists";
        String failureMsg = "Could not create account for: " + userName;
        try {
            if (catalogDb.findAccountByName(userName) != null) {
                throw new AccountException(acctExistsMsg);
            }
            catalogDb.createAccount(new Account(userName, catalogDb));
        } catch (Exception e) {
            throw new AccountException(failureMsg, e);
        }
    }

    @Override
    public synchronized AccountDTO getAccount(String userName) throws AccountException {
        if (userName == null) {
            return null;
        }

        try {
            return catalogDb.findAccountByName(userName);
        } catch (Exception e) {
            throw new AccountException("Could not search for account.", e);
        }
    }

    @Override
    public synchronized void deleteAccount(AccountDTO account) throws AccountException {
        try {
            catalogDb.deleteAccount(account);
        } catch (Exception e) {
            throw new AccountException("Could not delete account: " + account, e);
        }
    }

    @Override
    public synchronized void fileadding(AccountDTO acctDTO, int filenum, String filename, String url, int size, boolean access, boolean read, boolean write) throws RejectedException, AccountException {
        Account acct = (Account) getAccount(acctDTO.getUserName());
        acct.fileadding(filenum,filename,url,size,access, read,write);
        /*acct.filenumAdd(filenum);
        acct.filenameAdd(filename);
        acct.urlAdd(url);
        acct.sizeAdd(size);
        acct.accessAdd(access);
        acct.readAdd(read);
        acct.writeAdd(write); */
    }
     @Override
    public synchronized void filedelete(AccountDTO acctDTO, int filenum) throws RejectedException, AccountException {
        Account acct = (Account) getAccount(acctDTO.getUserName());
        acct.filedelete(filenum);
    }
}
