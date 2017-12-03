package se.kth.id1212.db.catalogjdbc.client.view;

import java.io.File;
import java.util.List;
import java.util.Scanner;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import se.kth.id1212.db.catalogjdbc.common.Catalog;
import se.kth.id1212.db.catalogjdbc.common.CatalogClient;
import se.kth.id1212.db.catalogjdbc.common.Credentials;
import se.kth.id1212.db.catalogjdbc.common.AccountDTO;
import se.kth.id1212.db.catalogjdbc.server.tcp.FileServer;
import se.kth.id1212.db.catalogjdbc.client.tcp.FileClient;
import se.kth.id1212.db.catalogjdbc.client.tcp.FileClientDownload;

/**
 * Reads and interprets user commands. The command interpreter will run in a
 * separate thread, which is started by calling the <code>start</code> method.
 * Commands are executed in a thread pool, a new prompt will be displayed as
 * soon as a command is submitted to the pool, without waiting for command
 * execution to complete.
 */
public class NonBlockingInterpreter implements Runnable {

    private static final String PROMPT = "> ";
    private final Scanner console = new Scanner(System.in);
    private final ThreadSafeStdOut outMgr = new ThreadSafeStdOut();
    private Catalog catalog;
    private final CatalogClient myRemoteObj;
    // private final CatalogClient thatRemoteObj;
    private int myIdAtServer;
    private boolean receivingCmds = false;

    public NonBlockingInterpreter() throws RemoteException {
        myRemoteObj = new ConsoleOutput(); //the current client
        //thatRemoteObj= new ConsoleOutput(); //the client to be notified of something
    }

    /**
     * Starts the interpreter. The interpreter will be waiting for user input
     * when this method returns. Calling <code>start</code> on an interpreter
     * that is already started has no effect.
     *
     * @param server The server with which this  client will communicate.
     */
    public void start(Catalog catalog) {
        this.catalog = catalog;
        if (receivingCmds) {
            return;
        }
        receivingCmds = true;
        new Thread(this).start();
    }

    /**
     * Interprets and performs user commands.
     */
    @Override
    public void run() {
        AccountDTO acct = null;
        AccountDTO acct01 = null;
        String remotnanodica = null;
        int OnaNodicaID = 0; //the id of a user to be notified when smb acceses their file
        CatalogClient toBeNotified; //the node of the user to be notified;
        int doesnotify = 0; //checks whether the specific user wants to be notified if someone access that specific file
        String accUserName = null; //the username of the acc that is to be notified
        while (receivingCmds) {
            try {
                CmdLine cmdLine = new CmdLine(readNextLine());
                switch (cmdLine.getCmd()) {
                    case HELP:
                        for (Command command : Command.values()) {
                            if (command == Command.ILLEGAL_COMMAND) {
                                continue;
                            }
                            System.out.println(command.toString().toLowerCase());
                        }
                        break;
                    /*case QUIT:
                        receivingCmds = false;
                    break;*/
                    case UPLOADFILE:
                        acct = catalog.getAcc(cmdLine.getParameter(0));
                        remotnanodica = catalog.RgetUsername(myIdAtServer);
                        if (remotnanodica.equalsIgnoreCase(acct.getUserName())) {
                            if (acct.getLoginStat() == 1) {
                                File myfilesize = new File(cmdLine.getParameter(2));
                                int fileSizeInBytes = (int) myfilesize.length();
                                catalog.addafil(cmdLine.getParameter(0), acct.getPassWord(), cmdLine.getParameter(1), cmdLine.getParameter(2), cmdLine.getParameter(3), fileSizeInBytes);
                                FileClient.clientTCP(cmdLine.getParameter(2), cmdLine.getParameter(3));
                            }
                        }
                        break;
                    case DOWNLOADFILE:
                        acct01 = catalog.getAcc(cmdLine.getParameter(0));
                        acct = catalog.getAccount(cmdLine.getParameter(1));
                        accUserName = acct.getUserName();//the username of the acc to be notified
                        remotnanodica = catalog.RgetUsername(myIdAtServer);
                        OnaNodicaID = catalog.RgetID(accUserName);  //GETS THE ID OF THE CLIENT THAT IS TO BE NOTIFIED
                        toBeNotified = catalog.RgetRemoteNode(OnaNodicaID);
                        doesnotify = acct.getAccess();
                        if (remotnanodica.equalsIgnoreCase(acct01.getUserName())) {
                            if ((acct.getUserName().equalsIgnoreCase(acct01.getUserName()) && acct.getLoginStat() == 1) || acct.getRead() == 1) {
                                FileClientDownload.clientTCPDownload(cmdLine.getParameter(2), cmdLine.getParameter(3));
                                if (doesnotify == 1 && !accUserName.equals(remotnanodica)) {
                                        toBeNotified.RrecvMsg("The user " + remotnanodica + " just downloaded your file,"+ acct.getFileName()+" and you wanted to be notified.");
                                    }
                            }
                        }
                        break;
                    case REGISTER:
                        if (catalog.getAcc(cmdLine.getParameter(0)) == (null)) {
                            catalog.createAccount(cmdLine.getParameter(0), cmdLine.getParameter(1), cmdLine.getParameter(2));
                        } else {
                            outMgr.println("Username already used. Choose another one.");
                        }
                        break;
                    case LOGIN:
                        catalog.loginAccount(cmdLine.getParameter(0), cmdLine.getParameter(1));
                        acct = catalog.getAcc(cmdLine.getParameter(0));
                        if (acct.getLoginStat() == 1) {
                            myIdAtServer = catalog.Rlogin(myRemoteObj,
                                    new Credentials(cmdLine.getParameter(0), cmdLine.getParameter(1)));
                            lookupServer(cmdLine.getParameter(2));
                        } else {
                            System.out.println("Invalid username and/or password");
                        }
                        break;
                    case LOGOUT:
                        acct = catalog.getAcc(cmdLine.getParameter(0));
                        remotnanodica = catalog.RgetUsername(myIdAtServer);
                        if (remotnanodica.equalsIgnoreCase(acct.getUserName())) {
                            catalog.logoutAccount(cmdLine.getParameter(0), cmdLine.getParameter(1));
                            receivingCmds = false;
                            catalog.Rlogout(myIdAtServer);
                            boolean forceUnexport = false;
                            UnicastRemoteObject.unexportObject(myRemoteObj, forceUnexport);
                        }
                        break;
                    case UNREGISTER:
                        acct = catalog.getAcc(cmdLine.getParameter(0));
                        remotnanodica = catalog.RgetUsername(myIdAtServer);
                        if (remotnanodica.equalsIgnoreCase(acct.getUserName())) {
                            catalog.deleteAccount(cmdLine.getParameter(0), cmdLine.getParameter(1));
                        }
                        break;
                    case LIST:
                        acct = catalog.getAcc(cmdLine.getParameter(0));
                        remotnanodica = catalog.RgetUsername(myIdAtServer);
                        if (remotnanodica.equalsIgnoreCase(acct.getUserName())) {
                            List<? extends AccountDTO> accounts = catalog.listAccounts();
                            for (AccountDTO account : accounts) {
                                if (account.getRead() == 1 || (acct.getLoginStat() == 1 && account.getUserName().equals(cmdLine.getParameter(0)))) {
                                    OnaNodicaID = catalog.RgetID(account.getUserName());  //GETS THE ID OF THE CLIENT THAT IS TO BE NOTIFIED
                                    toBeNotified = catalog.RgetRemoteNode(OnaNodicaID);
                                    doesnotify = account.getAccess();
                                    outMgr.println(account.getUserName() + ": FileNum:" + account.getFileNum() + "; Login:" + account.getLoginStat() + "; FileName:" + account.getFileName()
                                            + "; Url:" + account.getUrl() + "; Size:" + account.getSize() + "; Public access:" + account.getAccess() + "; ReadByEveryone:" + account.getRead()
                                            + "; WritebyEveryone:" + account.getWrite());
                                    if (doesnotify == 1 && !account.getUserName().equals(remotnanodica)) {
                                        toBeNotified.RrecvMsg("The user " + remotnanodica + " just read your file"+ account.getFileName()+ ", and you wanted to be notified.");
                                    }
                                }
                            }
                        }
                        break;
                    case UPDATEFILE:
                        acct01 = catalog.getAcc(cmdLine.getParameter(0));
                        acct = catalog.getAccount(cmdLine.getParameter(1));
                        accUserName = acct.getUserName();//the username of the acc to be notified
                        remotnanodica = catalog.RgetUsername(myIdAtServer);
                        OnaNodicaID = catalog.RgetID(accUserName);  //GETS THE ID OF THE CLIENT THAT IS TO BE NOTIFIED
                        toBeNotified = catalog.RgetRemoteNode(OnaNodicaID);
                        doesnotify = acct.getAccess();
                        if (remotnanodica.equalsIgnoreCase(acct01.getUserName())) {
                            if ((acct.getUserName().equalsIgnoreCase(acct01.getUserName()) && acct.getLoginStat() == 1) || acct.getWrite() == 1) {
                                File myfilename = new File("/Users/SasaLekic/Documents/TCPOutput/" + acct.getFileName());
                                myfilename.renameTo(new File("/Users/SasaLekic/Documents/TCPOutput/" + cmdLine.getParameter(3)));
                                catalog.fileupdating(acct, cmdLine.getParameter(2), cmdLine.getParameter(3),
                                        cmdLine.getParameter(4), Integer.parseInt(cmdLine.getParameter(5)), Integer.parseInt(cmdLine.getParameter(6)),
                                        Integer.parseInt(cmdLine.getParameter(7)), Integer.parseInt(cmdLine.getParameter(8)));
                                if (doesnotify == 1 && !accUserName.equals(remotnanodica)) {
                                    toBeNotified.RrecvMsg("The user " + remotnanodica + " just updated your file "+acct.getFileName()+", and you wanted to be notified.");
                                }
                            }
                        }
                        break;
                    /*!!!!!!! */ case DELETEFILE: //REMEMBER: getAcc is for getting a file by username, while getAccount is by filenum!!!!!
                        acct = catalog.getAcc(cmdLine.getParameter(0));
                        acct01=catalog.getAccount(cmdLine.getParameter(1)); //this finds the account who owns the file that is to be deleted
                        remotnanodica = catalog.RgetUsername(myIdAtServer);
                        OnaNodicaID = catalog.RgetID(accUserName);  //GETS THE ID OF THE CLIENT THAT IS TO BE NOTIFIED
                        toBeNotified = catalog.RgetRemoteNode(OnaNodicaID);
                        doesnotify = acct.getAccess();
                        if (remotnanodica.equalsIgnoreCase(acct.getUserName())) {
                            if (doesnotify == 1 && !acct01.getUserName().equals(remotnanodica)) {
                                    toBeNotified.RrecvMsg("The user " + remotnanodica + " just deleted your file "+acct.getFileName()+", and you wanted to be notified.");
                                }
                            catalog.filedelete(acct, cmdLine.getParameter(1));
                        }
                        break;
                        /* case FILEREAD://this lists all files from a specific user, this is NOT NECESSARY ???
                        
                        //acct = catalog.getAccountByFileName(cmdLine.getParameter(0));
                        if (acct.getRead() == 1) {
                        outMgr.println(acct.getFileNum() + acct.getFileName() + acct.getUrl() + Integer.toString(acct.getSize())
                        + Integer.toString(acct.getAccess()) + Integer.toString(acct.getRead()) + Integer.toString(acct.getWrite()));
                        } else {
                        outMgr.println("File not readable");
                        }
                        break;*/
                    default:
                        outMgr.println("Done");
                }
            } catch (Exception e) {
                outMgr.println("Operation failed");
                outMgr.println(e.getMessage());
                e.printStackTrace();
            }
        }
    }

    private void lookupServer(String host) throws NotBoundException, MalformedURLException,
            RemoteException {
        catalog = (Catalog) Naming.lookup(
                "//" + host + "/" + Catalog.CATALOG_NAME_IN_REGISTRY);
    }

    private String readNextLine() {
        outMgr.print(PROMPT);
        return console.nextLine();
    }

    private class ConsoleOutput extends UnicastRemoteObject implements CatalogClient {

        public ConsoleOutput() throws RemoteException {
        }

        @Override
        public void RrecvMsg(String msg) {
            outMgr.println((String) msg);
        }
    }

}
