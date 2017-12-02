package se.kth.id1212.db.catalogjdbc.server.model;

import se.kth.id1212.db.catalogjdbc.common.AccountDTO;
import se.kth.id1212.db.catalogjdbc.server.integration.CatalogDAO;

/**
 * An account in the catalog.
 */
public class Account implements AccountDTO {

    private String userName;
    private String passWord;
    private int loginStat;
    private String filenum;
    private String fileName;
    private String url;
    private int size;
    private int access;
    private int read;
    private int write;
    private transient CatalogDAO catalogDB;

    /**
     * Creates an account for the specified user with the specified stuff. The
     * account object will have a database connection.
     *
     * @param userName The account user's userName.
     * @param passWord
     * @param loginStat
     * @param filenum number of the file
     * @param filename name of the file
     * @param url url of the file
     * @param size size of the file
     * @param access whether public(1) or 0(not public)
     * @param read whether can be reade by others or not
     * @param write whether can be written to by others or not
     * @param catalogDB The DAO used to store updates to the database.
     */
    public Account(String userName, String passWord, int loginStat, String filenum, String filename, String url, int size, int access, int read, int write, CatalogDAO catalogDB) {
        this.userName = userName;
        this.passWord = passWord;
        this.loginStat = loginStat;
        this.filenum = filenum;
        this.fileName = filename;
        this.url = url;
        this.size = size;
        this.access = access;
        this.read = read;
        this.write = write;
        this.catalogDB = catalogDB;
    }

    /**
     * Creates an account for the specified user with the specified stuff. The
     * account object will not have a database connection.
     *
     * @param userName The account user's userName.
     * @param passWord
     * @param loginStat
     * @param filenum number of the file
     * @param filename name of the file
     * @param url url of the file
     * @param size size of the file
     * @param access whether public(1) or 0(not public)
     * @param read whether can be reade by others or not
     * @param write whether can be written to by others or not
     */
    public Account(String userName, String passWord, int loginStat, String filenum, String filename, String url, int size, int access, int read, int write) {
        this(userName, passWord, loginStat, filenum, filename, url, size, access, read, write, null);
    }

    /**
     * Creates an account for the specified user with the stuff zero.
     *
     * @param userName The account user's userName.
     * @param passWord
     * @param filenum
     * @param catalogDB The DAO used to store updates to the database.
     */
    public Account(String userName, String passWord, String filenum, CatalogDAO catalogDB) {
        this(userName, passWord, 0, filenum, null, null, 0, 0, 0, 0, catalogDB);
    }

    //this constructor is for adding a file
    public Account(String userName, String passWord, String filenum, String url, String filename, CatalogDAO catalogDB) {
        this(userName, passWord, 0, filenum, filename, url, 0, 0, 0, 0, catalogDB);
    }

    public void filedelete(String filenum) throws RejectedException {
        try {
            catalogDB.deleteUpdateAccountFile(this);
        } catch (Exception e) {
            throw new RejectedException("Faillll" + accountInfo(), e);
        }
    }
        /**
         * THIS IS USED FOR EDITING A FILE.
         *
         * @param filenum
         * @param filename
         * @param url
         * @param size
         * @param access
         * @param read
         * @param write
         * @throws AccountException If the specified num is negative, or if
         * unable to perform the update.
         */
    public void fileupdating(String filenum, String filename, String url, int size, int access, int read, int write) throws RejectedException {
        /*if (filenum < 0) {
        throw new RejectedException(
        "Tried to add negative value of filenum, illegal value: " + filenum + "." + accountInfo());
        }*/
        changeFileInfo(filenum, filename, url, size, access, read, write, "Could not add the file.");
    }

    private void changeFileInfo(String newfilenum, String newfilename, String newurl, int newsize,
            int newaccess, int newread, int newwrite, String failureMsg) throws RejectedException {
        String initialpassword = this.passWord;
        int initiallogin = this.loginStat;
        String initialfilenum = filenum;
        String initialfileName = fileName;
        String initialurl = url;
        int initialsize = size;
        int initialaccess = access;
        int initialread = read;
        int initialwrite = write;

        try {
            passWord = initialpassword;
            loginStat = initiallogin;
            filenum = newfilenum;
            fileName = newfilename;
            url = newurl;
            size = newsize;
            access = newaccess;
            read = newread;
            write = newwrite;
            catalogDB.updateAccount(this);
        } catch (Exception e) {
            filenum = initialfilenum;
            fileName = initialfileName;
            url = initialurl;
            size = initialsize;
            access = initialaccess;
            read = initialread;
            write = initialwrite;
            throw new RejectedException(failureMsg + accountInfo(), e);
        }
    }

    private String accountInfo() {
        return " " + this;
    }

    public String getFileNum() {
        return filenum;
    }

    public String getFileName() {
        return fileName;
    }

    public String getUrl() {
        return url;
    }

    public int getSize() {
        return size;
    }

    public int getAccess() {
        return access;
    }

    public int getRead() {
        return read;
    }

    public int getWrite() {
        return write;
    }

    /**
     * @return The user's name.
     */
    public String getUserName() {
        return userName;
    }

    public String getPassWord() {
        return passWord;
    }

    public int getLoginStat() {
        return loginStat;
    }

    /**
     * @return A string representation of all fields in this object.
     */
    @Override
    public String toString() {
        StringBuilder stringRepresentation = new StringBuilder();
        stringRepresentation.append("Account: [");
        stringRepresentation.append("user: ");
        stringRepresentation.append(userName);
        stringRepresentation.append(", filenum: ");
        stringRepresentation.append(filenum);
        stringRepresentation.append(", fileName: ");
        stringRepresentation.append(fileName);
        stringRepresentation.append(", url: ");
        stringRepresentation.append(url);
        stringRepresentation.append(", size: ");
        stringRepresentation.append(size);
        stringRepresentation.append(", access by everyone: ");
        stringRepresentation.append(access);
        stringRepresentation.append(", can be read by anyone: ");
        stringRepresentation.append(read);
        stringRepresentation.append(", can be written to by anyone: ");
        stringRepresentation.append(write);
        stringRepresentation.append("]");
        return stringRepresentation.toString();
    }
}
