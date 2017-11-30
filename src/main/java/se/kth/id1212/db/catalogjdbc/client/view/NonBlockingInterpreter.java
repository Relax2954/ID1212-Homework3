
package se.kth.id1212.db.catalogjdbc.client.view;

import java.util.List;
import java.util.Scanner;
import se.kth.id1212.db.catalogjdbc.common.Catalog;
import se.kth.id1212.db.catalogjdbc.common.AccountDTO;

/**
 * Reads and interprets user commands. The command interpreter will run in a separate thread, which
 * is started by calling the <code>start</code> method. Commands are executed in a thread pool, a
 * new prompt will be displayed as soon as a command is submitted to the pool, without waiting for
 * command execution to complete.
 */
public class NonBlockingInterpreter implements Runnable {
    private static final String PROMPT = "> ";
    private final Scanner console = new Scanner(System.in);
    private final ThreadSafeStdOut outMgr = new ThreadSafeStdOut();
    private Catalog catalog;
    private boolean receivingCmds = false;

    /**
     * Starts the interpreter. The interpreter will be waiting for user input when this method
     * returns. Calling <code>start</code> on an interpreter that is already started has no effect.
     *
     * @param server The server with which this chat client will communicate.
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
                    case QUIT:
                        receivingCmds = false;
                        break;
                    case NEW:
                        catalog.createAccount(cmdLine.getParameter(0));
                        ;
                        break;
                    case DELETE:
                        acct = catalog.getAccount(cmdLine.getParameter(0));
                        catalog.deleteAccount(acct);
                        break;
                    case LIST:
                        List<? extends AccountDTO> accounts = catalog.listAccounts();
                        for (AccountDTO account : accounts) {
                            outMgr.println(account.getUserName() + ": FileNum:" + account.getFileNum() + "; FileName:" + account.getFileName()
                                    + "; Url:" + account.getUrl() + "; Size:" + account.getSize() + "; Public access:" + account.getAccess() + "; ReadByEveryone:" + account.getRead()
                                    + "; WritebyEveryone:" + account.getWrite());
                        }
                        break;
                    case ADDFILE:
                        acct = catalog.getAccount(cmdLine.getParameter(0));
                        catalog.fileadding(acct, Integer.parseInt(cmdLine.getParameter(1)), cmdLine.getParameter(2),
                                cmdLine.getParameter(3), Integer.parseInt(cmdLine.getParameter(4)), Boolean.parseBoolean(cmdLine.getParameter(5)),
                                Boolean.parseBoolean(cmdLine.getParameter(6)), Boolean.parseBoolean(cmdLine.getParameter(7)));
                        break;
                    case DELETEFILE:
                        acct = catalog.getAccount(cmdLine.getParameter(0));
                        catalog.filedelete(acct, Integer.parseInt(cmdLine.getParameter(1)));
                        break;
                    case FILELIST://this lists all files from a specific user, this is NOT NECESSARY ???
                        acct = catalog.getAccount(cmdLine.getParameter(0));
                        outMgr.println(Integer.toString(acct.getFileNum()) + acct.getFileName() + acct.getUrl() + Integer.toString(acct.getSize())
                                + Boolean.toString(acct.getAccess()) + Boolean.toString(acct.getRead()) + Boolean.toString(acct.getWrite()));
                        break;
                    default:
                        outMgr.println("illegal command");
                }
            } catch (Exception e) {
                outMgr.println("Operation failed");
                outMgr.println(e.getMessage());
            }
        }
    }

    private String readNextLine() {
        outMgr.print(PROMPT);
        return console.nextLine();
    }
}
