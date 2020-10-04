package paq;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Mac;
import javax.imageio.ImageIO;

public class ThreadServidor implements Runnable {
	
	private Socket socket;
	
	public ThreadServidor (Socket psocket){
		socket = psocket;
	}
	
	public void cambiarSocket(){
		
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		OutputStream outputStream;
		try {
			outputStream = socket.getOutputStream();
			BufferedReader image = new BufferedReader(new FileReader(new File("./Files/prueba.txt")));
			long hash1 = image.hashCode();
			long hash2 = image.hashCode();
			
			

	        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
	        ImageIO.write(image, "jpg", byteArrayOutputStream);

	        byte[] size = ByteBuffer.allocate(8).putInt(byteArrayOutputStream.size()).array();
	        outputStream.write(size);
	        outputStream.write(byteArrayOutputStream.toByteArray());
	        System.out.println("hash1: "+byteArrayOutputStream.toByteArray().hashCode());
	        outputStream.flush();
	        System.out.println("Flushed: " + System.currentTimeMillis());

	        socket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

        
	}
	
	public static byte[] hdg(byte[] msg, String algo) throws NoSuchAlgorithmException, InvalidKeyException, IllegalStateException, UnsupportedEncodingException {
	    Mac mac = Mac.getInstance(algo);
	    
	    byte[] bytes = mac.doFinal(msg);
	    return bytes;
	}

	public static boolean vi(byte[] msg, String algo, byte[] hash) throws Exception {
	    byte[] nuevo = hdg(msg, algo);
	    if (nuevo.length != hash.length)
	      return false; 
	    for (int i = 0; i < nuevo.length; i++) {
	      if (nuevo[i] != hash[i])
	        return false; 
	    } 
	    return true;
	  }


}
