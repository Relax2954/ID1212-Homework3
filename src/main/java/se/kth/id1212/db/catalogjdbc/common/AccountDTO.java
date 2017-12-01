package se.kth.id1212.db.catalogjdbc.common;

import java.io.Serializable;

/**
 * Specifies a read-only view of an account.
 */
public interface AccountDTO extends Serializable {
       //returns the name of the user
    public String getUserName();
    
    public String getPassWord();
    //checks the login status
    public int getLoginStat();
    //returns the number of the file
   public int getFileNum();
    
   //returns the name of the file
   public String getFileName();
   
   //returns the url of the file
   public String getUrl();
   
   //returns the size of the file
   public int getSize();
   
   //returns whether the file can be publically accesed(true) or not
   public int getAccess();
   
   //returns whether a file can be read by anyone or not
   public int getRead();
   
   //returns whether a file can be written to by anyone or not
   public int getWrite();
}
