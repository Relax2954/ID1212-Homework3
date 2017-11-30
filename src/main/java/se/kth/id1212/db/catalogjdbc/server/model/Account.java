package se.kth.id1212.db.catalogjdbc.server.model;

import se.kth.id1212.db.catalogjdbc.common.AccountDTO;
import se.kth.id1212.db.catalogjdbc.server.integration.CatalogDAO;

/**
 * An account in the catalog.
 */
public class Account implements AccountDTO {

    private String userName;
    private int filenum;
    private String fileName;
    private String url;
    private int size;
    private boolean access;
    private boolean read;
    private boolean write;
    private transient CatalogDAO catalogDB;

    /**
     * Creates an account for the specified user with the specified stuff. The
     * account object will have a database connection.
     *
     * @param userName The account user's userName.
     * @param filenum number of the file
     * @param filename name of the file
     * @param url url of the file
     * @param size size of the file
     * @param access whether public(1) or 0(not public)
     * @param read whether can be reade by others or not
     * @param write whether can be written to by others or not
     * @param catalogDB The DAO used to store updates to the database.
     */
    public Account(String userName, int filenum, String filename, String url, int size, boolean access, boolean read, boolean write, CatalogDAO catalogDB) {
        this.userName = userName;
        this.filenum = filenum;
        this.fileName = filename;
        this.url = url;
        this.access = access;
        this.read = read;
        this.write = write;
    }

    /**
     * Creates an account for the specified user with the specified stuff. The
     * account object will not have a database connection.
     *
     * @param userName The account user's userName.
     * @param filenum number of the file
     * @param filename name of the file
     * @param url url of the file
     * @param size size of the file
     * @param access whether public(1) or 0(not public)
     * @param read whether can be reade by others or not
     * @param write whether can be written to by others or not
     */
    public Account(String userName, int filenum, String filename, String url, int size, boolean access, boolean read, boolean write) {
        this(userName, filenum, filename, url, size, access, read, write, null);
    }

    /**
     * Creates an account for the specified user with the stuff zero.
     *
     * @param userName The account user's userName.
     * @param catalogDB The DAO used to store updates to the database.
     */
    public Account(String userName, CatalogDAO catalogDB) {
        this(userName, 0, null, null, 0, false, false, false, catalogDB);
    }

    /**
     * Adds the specified file.
     *
     * @param filenum
     * @param filename
     * @param url
     * @param size
     * @param access
     * @param read
     * @param write
     * @throws AccountException If the specified num is negative, or if unable
     * to perform the update.
     */
    public void fileadding(int filenum, String filename, String url, int size, boolean access, boolean read, boolean write) throws RejectedException {
        if (filenum < 0) {
            throw new RejectedException(
                    "Tried to add negative value of filenum, illegal value: " + filenum + "." + accountInfo());
        }
        changeFileInfo(filenum, filename, url, size, access, read, write, "Could not add the file.");
    }

    public void filedelete(int filenum) throws RejectedException {
        if (filenum < 0) {
            throw new RejectedException(
                    "Tried to delete a non-existant file, illegal value: " + filenum + "." + accountInfo());
        }

        changeFileInfo(0, null, null, 0, false, false, false, "Could not delete the file.");
    }

    private void changeFileInfo(int newfilenum, String newfilename, String newurl, int newsize,
            boolean newaccess, boolean newread, boolean newwrite, String failureMsg) throws RejectedException {
        int initialfilenum = filenum;
        String initialfileName = fileName;
        String initialurl = url;
        int initialsize = size;
        boolean initialaccess = access;
        boolean initialread = read;
        boolean initialwrite = write;

        try {
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

    public int getFileNum() {
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

    public boolean getAccess() {
        return access;
    }

    public boolean getRead() {
        return read;
    }

    public boolean getWrite() {
        return write;
    }

    /**
     * @return The user's name.
     */
    public String getUserName() {
        return userName;
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
