/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.kth.id1212.db.catalogjdbc.client.tcp;
import se.kth.id1212.db.catalogjdbc.server.tcp.FileServer;

import java.net.*;  
import java.io.*;  
   
public class FileClient {  
       
    public static void clientTCP(String urlInput, String nameInput) throws IOException {  
   
        Socket sock = new Socket("127.0.0.1", 13267);  
   
        //Send file  
        File myFileDir = new File(urlInput);
        String myFileName=nameInput;
        byte[] mybytearray = new byte[(int) myFileDir.length()];  
           
        FileInputStream fis = new FileInputStream(myFileDir);  
        BufferedInputStream bis = new BufferedInputStream(fis);  
        //bis.read(mybytearray, 0, mybytearray.length);  
           
        DataInputStream dis = new DataInputStream(bis);     
        dis.readFully(mybytearray, 0, mybytearray.length);  
           
        OutputStream os = sock.getOutputStream();  
           
        //Sending file name and file size to the server  
        DataOutputStream dos = new DataOutputStream(os);     
        //dos.writeUTF(myFileDir.getName());    
        dos.writeUTF(myFileName);
        dos.writeLong(mybytearray.length);     
        dos.write(mybytearray, 0, mybytearray.length);     
        dos.flush();  
           
        //Sending file data to the server  
        os.write(mybytearray, 0, mybytearray.length);  
        os.flush();  
           
        //Closing socket
        os.close();
        dos.close();  
        sock.close();  
    }  
}