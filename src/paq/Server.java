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
    	 System.out.println("AAAAAAARNOOOOOLD2!!!!!"); 


         while(true){
        	 System.out.println("AAAAAAARNOOOOOLD3!!!!!"); 


             System.out.println("Esperando conexión ..."); 
             socket = server.accept(); 
             pool.execute(new ThreadServidor(socket));
             System.out.println("Cliente aceptado"); 
             
             in = new DataInputStream( 
                 new BufferedInputStream(socket.getInputStream())); 

             String line = ""; 

           
             System.out.println("Cerrando Conexión"); 
 
         }

     } 
     catch(IOException i) 
     { 
         System.out.println(i.getMessage());
         i.printStackTrace();
     } 
 } 

 public static void main(String args[]) 
 { 
     Server server = new Server(5002); 
 } 
} 
