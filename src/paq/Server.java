package paq;

import java.net.*;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.io.*; 

public class Server 
{ 
 private Socket          socket   = null; 
 private ServerSocket    server   = null; 
 private DataInputStream in       =  null; 
 private ArrayList<ThreadServidor> arregloThreads = null;

 public Server(int port) 
 { 
     try
     { 
         server = new ServerSocket(port);
         ExecutorService pool = Executors.newFixedThreadPool(25);
         
    	 System.out.println("Servidor Iniciado"); 

         while(true){

             System.out.println("Esperando conexión ..."); 
             socket = server.accept(); 
             System.out.println("Cliente aceptado"); 
             
             in = new DataInputStream( 
                 new BufferedInputStream(socket.getInputStream())); 

             String line = ""; 

             while (!line.equals("Terminado")) 
             { 
                 try
                 { 
                     line = in.readUTF(); 
                     System.out.println(line); 

                 } 
                 catch(IOException i) 
                 {
                	 i.getMessage(); 
                 } 
             } 
             System.out.println("Cerrando Conexión"); 

             socket.close(); 
             in.close(); 
         }
         
         
     } 
     catch(IOException i) 
     { 
         i.getMessage(); 
     } 
 } 

 public static void main(String args[]) 
 { 
     Server server = new Server(5000); 
 } 
} 
