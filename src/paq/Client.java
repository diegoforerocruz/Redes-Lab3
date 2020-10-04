package paq;

//A Java program for a Client 
import java.net.*;
import java.nio.ByteBuffer;

import javax.imageio.ImageIO;

import java.awt.image.BufferedImage;
import java.io.*; 

public class Client 
{ 
 // initialize socket and input output streams 
 private Socket socket            = null; 
 private DataInputStream  input   = null; 
 private DataOutputStream out     = null; 

 // constructor to put ip address and port 
 public Client(String address, int port) 
 { 
     // establish a connection 
     try
     { 
         socket = new Socket(address, port); 
         System.out.println("Conectado"); 
         InputStream inputStream = socket.getInputStream();

         System.out.println("Reading: " + System.currentTimeMillis());

         byte[] sizeAr = new byte[8];
         inputStream.read(sizeAr);
         int size = ByteBuffer.wrap(sizeAr).asIntBuffer().get();

         byte[] imageAr = new byte[size];
         inputStream.read(imageAr);

         BufferedImage image = ImageIO.read(new ByteArrayInputStream(imageAr));

         System.out.println("Received " + image.getHeight() + "x" + image.getWidth() + ": " + System.currentTimeMillis());
         ImageIO.write(image, "jpg", new File("./Files/"+port+".jpg"));

         socket.close();
     

         // takes input from terminal 
         input  = new DataInputStream(System.in); 

         // sends output to the socket 
         out    = new DataOutputStream(socket.getOutputStream()); 
     } 
     catch(UnknownHostException u) 
     { 
         System.out.println(u); 
     } 
     catch(IOException i) 
     { 
         System.out.println(i); 
     } 

     // string to read message from input 
     String line = ""; 

     // keep reading until "Over" is input 
     while (!line.equals("Over")) 
     { 
         try
         { 
             line = input.readLine(); 
             out.writeUTF(line); 
         } 
         catch(IOException i) 
         { 
             System.out.println(i); 
         } 
     } 

     // close the connection 
     try
     { 
         input.close(); 
         out.close(); 
         socket.close(); 
     } 
     catch(IOException i) 
     { 
         System.out.println(i); 
     } 
 } 

 public static void main(String args[]) 
 { 
     Client client = new Client("127.0.0.1", 5002); 
     
     
     
 } 
} 

