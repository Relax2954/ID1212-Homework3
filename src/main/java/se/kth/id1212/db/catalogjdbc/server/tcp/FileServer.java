/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.kth.id1212.db.catalogjdbc.server.tcp;
import se.kth.id1212.db.catalogjdbc.client.tcp.FileClient;

import java.net.*;     
import java.io.*;     
     
public class FileServer {      
     
  public static void main (String[] args ) throws IOException {     
       
    int bytesRead;  
    int current = 0;  
   
    ServerSocket serverSocket = null;  
    serverSocket = new ServerSocket(13267);  
         
    while(true) {  
        Socket clientSocket = null;  
        clientSocket = serverSocket.accept();  
           
        InputStream in = clientSocket.getInputStream();  
           
        DataInputStream clientData = new DataInputStream(in);   
           
        String mainDir = "/Users/SasaLekic/Documents/TCPOutput/";  
        String fileName = clientData.readUTF();  
        OutputStream output = new FileOutputStream(mainDir+fileName);     
        long size = clientData.readLong();     
        byte[] buffer = new byte[1024];     
        while (size > 0 && (bytesRead = clientData.read(buffer, 0, (int)Math.min(buffer.length, size))) != -1)     
        {     
            output.write(buffer, 0, bytesRead);     
            size -= bytesRead;     
        }  
           
        // Closing the FileOutputStream handle
        in.close();
        clientData.close();
        output.close();  
    }  
  }  
}  