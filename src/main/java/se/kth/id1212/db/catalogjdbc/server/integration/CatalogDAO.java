package se.kth.id1212.db.catalogjdbc.server.integration;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import se.kth.id1212.db.catalogjdbc.server.model.Account;
import se.kth.id1212.db.catalogjdbc.common.AccountDTO;

/**
 * This data access object (DAO) encapsulates all database calls in the catalog
 * application. No code outside this class shall have any knowledge about the
 * database.
 */
public class CatalogDAO {

    private static final String TABLE_NAME = "ACCOUNT";
    private static final String FILENUM_COLUMN_NAME = "FILENUM";
    private static final String FILEE_COLUMN_NAME = "FILENAME";
    private static final String URL_COLUMN_NAME = "URL";
    private static final String USER_COLUMN_NAME = "NAME";
    private static final String PASSWORD_COLUMN_NAME = "PASSWORD";
    private static final String LOGINSTAT_COLUMN_NAME = "LOGINSTAT";
    private static final String SIZE_COLUMN_NAME = "SIZE";
    private static final String ACCESS_COLUMN_NAME = "ACCESS";
    private static final String READ_COLUMN_NAME = "READD";
    private static final String WRITE_COLUMN_NAME = "WRITEE";

    private PreparedStatement createAccountStmt;
    private PreparedStatement findAccountStmt;
    private PreparedStatement findAllAccountsStmt;
    private PreparedStatement deleteAccountStmt;
    private PreparedStatement updateFileStmt;
    private PreparedStatement loginAccountStmt;
    private PreparedStatement findAccountNOMStmt;
    private PreparedStatement addFileStmt;
    private PreparedStatement deleteFileStmt;

    /**
     * Constructs a new DAO object connected to the specified database.
     *
     * @param dbms Database management system vendor. Currently supported types
     * are "derby" and "mysql".
     * @param datasource Database name.
     */
    public CatalogDAO(String dbms, String datasource) throws CatalogDBException {
        try {
            Connection connection = createDatasource(dbms, datasource);
            prepareStatements(connection);
        } catch (ClassNotFoundException | SQLException exception) {
            throw new CatalogDBException("Could not connect to datasource.", exception);
        }
    }

    /**
     * Searches for an account whose user has the specified name.
     *
     * @param userName The account user's name
     * @return The account whose user has the specified name, or
     * <code>null</code> if there is no such account.
     * @throws CatalogDBException If failed to search for account.
     */
    public Account findAccountByName(String filenum) throws CatalogDBException {
        String failureMsg = "Could not search for specified account.";
        ResultSet result = null;
        try {
            findAccountStmt.setString(1, filenum);
            result = findAccountStmt.executeQuery();
            if (result.next()) {
                return new Account(result.getString(USER_COLUMN_NAME), result.getString(PASSWORD_COLUMN_NAME), result.getInt(LOGINSTAT_COLUMN_NAME), filenum, result.getString(FILEE_COLUMN_NAME),
                        result.getString(URL_COLUMN_NAME), result.getInt(SIZE_COLUMN_NAME), result.getInt(ACCESS_COLUMN_NAME),
                        result.getInt(READ_COLUMN_NAME), result.getInt(WRITE_COLUMN_NAME), this);
            }
        } catch (SQLException sqle) {
            throw new CatalogDBException(failureMsg, sqle);
        } finally {
            try {
                result.close();
            } catch (Exception e) {
                throw new CatalogDBException(failureMsg, e);
            }
        }
        return null;
    }

    public Account findAccountByNom(String name) throws CatalogDBException {
        String failureMsg = "Could not search for specified account.";
        ResultSet result = null;
        try {
            findAccountNOMStmt.setString(1, name);
            result = findAccountNOMStmt.executeQuery();
            if (result.next()) {
                return new Account(name, result.getString(PASSWORD_COLUMN_NAME), result.getInt(LOGINSTAT_COLUMN_NAME), result.getString(FILENUM_COLUMN_NAME), result.getString(FILEE_COLUMN_NAME),
                        result.getString(URL_COLUMN_NAME), result.getInt(SIZE_COLUMN_NAME), result.getInt(ACCESS_COLUMN_NAME),
                        result.getInt(READ_COLUMN_NAME), result.getInt(WRITE_COLUMN_NAME), this);
            }
        } catch (SQLException sqle) {
            throw new CatalogDBException(failureMsg, sqle);
        } finally {
            try {
                result.close();
            } catch (Exception e) {
                throw new CatalogDBException(failureMsg, e);
            }
        }
        return null;
    }

    /**
     * Retrieves all existing accounts.
     *
     * @return A list with all existing accounts. The list is empty if there are
     * no accounts.
     * @throws CatalogDBException If failed to search for account.
     */
    public List<Account> findAllAccounts() throws CatalogDBException {
        String failureMsg = "Could not list accounts.";
        List<Account> accounts = new ArrayList<>();
        try (ResultSet result = findAllAccountsStmt.executeQuery()) {
            while (result.next()) {
                accounts.add(new Account(result.getString(USER_COLUMN_NAME), result.getString(PASSWORD_COLUMN_NAME), result.getInt(LOGINSTAT_COLUMN_NAME), result.getString(FILENUM_COLUMN_NAME),
                        result.getString(FILEE_COLUMN_NAME), result.getString(URL_COLUMN_NAME), result.getInt(SIZE_COLUMN_NAME),
                        result.getInt(ACCESS_COLUMN_NAME), result.getInt(READ_COLUMN_NAME), result.getInt(WRITE_COLUMN_NAME)));
            }
        } catch (SQLException sqle) {
            throw new CatalogDBException(failureMsg, sqle);
        }
        return accounts;
    }

    /**
     * Creates a new account.
     *
     * @param account The account to create.
     * @throws CatalogDBException If failed to create the specified account.
     */
    public void addafil(AccountDTO account) throws CatalogDBException {
        String failureMsg = "Could not create the file: " + account;
        try {
            addFileStmt.setString(1, account.getUserName());
            addFileStmt.setString(2, account.getPassWord());
            addFileStmt.setInt(3, 1);
            addFileStmt.setString(4, account.getFileNum());
            addFileStmt.setString(5, account.getFileName());
            addFileStmt.setString(6, account.getUrl());
            addFileStmt.setInt(7, account.getSize());
            addFileStmt.setInt(8, account.getAccess());
            addFileStmt.setInt(9, account.getRead());
            addFileStmt.setInt(10, account.getWrite());
            int rows = addFileStmt.executeUpdate();
            if (rows != 1) {
                throw new CatalogDBException(failureMsg);
            }
        } catch (SQLException sqle) {
            throw new CatalogDBException(failureMsg, sqle);
        }
    }

    public void createAccount(AccountDTO account) throws CatalogDBException {
        String failureMsg = "Could not create the account: " + account;
        try {
            createAccountStmt.setString(1, account.getUserName());
            createAccountStmt.setString(2, account.getPassWord());
            createAccountStmt.setInt(3, account.getLoginStat());
            createAccountStmt.setString(4, account.getFileNum());
            createAccountStmt.setString(5, account.getFileName());
            createAccountStmt.setString(6, account.getUrl());
            createAccountStmt.setInt(7, account.getSize());
            createAccountStmt.setInt(8, account.getAccess());
            createAccountStmt.setInt(9, account.getRead());
            createAccountStmt.setInt(10, account.getWrite());
            int rows = createAccountStmt.executeUpdate();
            if (rows != 1) {
                throw new CatalogDBException(failureMsg);
            }
        } catch (SQLException sqle) {
            throw new CatalogDBException(failureMsg, sqle);
        }
    }
    
        public void loginAccount(AccountDTO account, String pass) throws CatalogDBException, SQLException {
        String failureMsg = "Could not login intoo account: " + account;
        if (account.getPassWord().equals(pass)) {
            loginAccountStmt.setInt(1, 1);
            loginAccountStmt.setInt(2, 1);
            loginAccountStmt.setString(3, account.getUserName());
            loginAccountStmt.executeUpdate();
        }
    }

    public void deleteAccount(AccountDTO account, String pass) throws CatalogDBException, SQLException {
        String failureMsg = "Could delete account: " + account;
        if (account.getPassWord().equals(pass)) {
            deleteAccountStmt.setString(1, account.getUserName());
            deleteAccountStmt.executeUpdate();
        }
    }

    public void logoutAccount(AccountDTO account) throws CatalogDBException, SQLException {
        String failureMsg = "Could not login out of account: " + account;
        loginAccountStmt.setInt(1, 0);
        loginAccountStmt.setInt(2, 0);
        loginAccountStmt.setString(3, account.getUserName());
        loginAccountStmt.executeUpdate();
    }


    /**
     * Updates the specified account to the values of the field sin the
     * specified <code>AccountDTO</code>. The account is identified by its user
     * name.
     *
     * @param account The account to update.
     * @throws CatalogDBException If unable to update the specified account.
     */
    
    public void deleteUpdateAccountFile(AccountDTO account) throws CatalogDBException {
       try { 
    deleteFileStmt.setString(1, account.getFileNum());
    deleteFileStmt.executeUpdate();
     } catch (SQLException sqle) {
            throw new CatalogDBException("Could not delete file from account: " + account, sqle);
        }
    }
    public void updateAccount(AccountDTO account) throws CatalogDBException {
        try {
            updateFileStmt.setString(1, account.getPassWord());
            updateFileStmt.setInt(2, account.getLoginStat());
            updateFileStmt.setString(3, account.getFileName());
            updateFileStmt.setString(4, account.getUrl());
            updateFileStmt.setInt(5, account.getSize());
            updateFileStmt.setInt(6, account.getAccess());
            updateFileStmt.setInt(7, account.getRead());
            updateFileStmt.setInt(8, account.getWrite());
            updateFileStmt.setString(9, account.getUserName());
            updateFileStmt.setString(10, account.getFileNum());
            updateFileStmt.executeUpdate();
        } catch (SQLException sqle) {
            throw new CatalogDBException("Could not update the account: " + account, sqle);
        }
    }

    private Connection createDatasource(String dbms, String datasource) throws
            ClassNotFoundException, SQLException, CatalogDBException {
        Connection connection = connectToCatalogDB(dbms, datasource);
        if (!catalogTableExists(connection)) {
            Statement statement = connection.createStatement();
            statement.executeUpdate("CREATE TABLE " + TABLE_NAME
                    + " (" + USER_COLUMN_NAME + " VARCHAR(32) , " + PASSWORD_COLUMN_NAME + " VARCHAR(32), " + LOGINSTAT_COLUMN_NAME + " FLOAT, "
                    + FILENUM_COLUMN_NAME + " VARCHAR(32) PRIMARY KEY , " + FILEE_COLUMN_NAME + " VARCHAR(32), "
                    + URL_COLUMN_NAME + " VARCHAR(250), " + SIZE_COLUMN_NAME + " FLOAT, " + ACCESS_COLUMN_NAME + " INT, "
                    + READ_COLUMN_NAME + " INT, " + WRITE_COLUMN_NAME + " INT ) ");

        
        }
        return connection;
    }

    private boolean catalogTableExists(Connection connection) throws SQLException {
        int tableNameColumn = 3;
        DatabaseMetaData dbm = connection.getMetaData();
        try (ResultSet rs = dbm.getTables(null, null, null, null)) {
            for (; rs.next();) {
                if (rs.getString(tableNameColumn).equals(TABLE_NAME)) {
                    return true;
                }
            }
            return false;
        }
    }

    private Connection connectToCatalogDB(String dbms, String datasource)
            throws ClassNotFoundException, SQLException, CatalogDBException {
        if (dbms.equalsIgnoreCase("derby")) {
            Class.forName("org.apache.derby.jdbc.ClientXADataSource");
            return DriverManager.getConnection(
                    "jdbc:derby://localhost:1527/" + datasource + ";create=true");
        } else if (dbms.equalsIgnoreCase("mysql")) {
            Class.forName("com.mysql.jdbc.Driver");
            return DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/" + datasource, "user", "password");
        } else {
            throw new CatalogDBException("Unable to create datasource, unknown dbms.");
        }
    }

    private void prepareStatements(Connection connection) throws SQLException {
        createAccountStmt = connection.prepareStatement("INSERT INTO "
                + TABLE_NAME + " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ? )");
        addFileStmt = connection.prepareStatement("INSERT INTO "
                + TABLE_NAME + " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ? )");
        findAccountStmt = connection.prepareStatement("SELECT * from "
                + TABLE_NAME + " WHERE FILENUM = ?");
        findAccountNOMStmt = connection.prepareStatement("SELECT * from "
                + TABLE_NAME + " WHERE NAME = ?");
        findAllAccountsStmt = connection.prepareStatement("SELECT * from "
                + TABLE_NAME);
        deleteAccountStmt = connection.prepareStatement("DELETE FROM "
                + TABLE_NAME
                + " WHERE name = ?");
        loginAccountStmt = connection.prepareStatement("UPDATE " + TABLE_NAME + " SET loginstat=?, access=?  WHERE name=? ");
        updateFileStmt = connection.prepareStatement("UPDATE "
                + TABLE_NAME
                + " SET  password=?, loginstat=?,  filename=?,  url=?,  size=?,  access=?, "
                + " readd=?, writee=?  WHERE (name= ? AND filenum=?) ");
        deleteFileStmt = connection.prepareStatement("DELETE FROM "
                + TABLE_NAME
                + " WHERE filenum = ?");
    }

}
