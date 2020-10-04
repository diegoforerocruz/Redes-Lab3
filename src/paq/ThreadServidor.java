package paq;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;

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
			BufferedImage image = ImageIO.read(new File("./Files/succ.jpg"));

	        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
	        ImageIO.write(image, "jpg", byteArrayOutputStream);

	        byte[] size = ByteBuffer.allocate(4).putInt(byteArrayOutputStream.size()).array();
	        outputStream.write(size);
	        outputStream.write(byteArrayOutputStream.toByteArray());
	        outputStream.flush();
	        System.out.println("Flushed: " + System.currentTimeMillis());

	        socket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

        
	}



}
