package paq;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.security.MessageDigest;

public class ThreadServidor implements Runnable {

	private Socket socket;
	private String documento;
	private DataOutputStream os;
	private DataInputStream is;
	private MessageDigest md;
	
	public ThreadServidor(Socket socket, String documento) throws Exception {
		this.socket = socket;
		this.documento = documento;
		os = new DataOutputStream(socket.getOutputStream());
		is = new DataInputStream(socket.getInputStream());
		md = MessageDigest.getInstance(Server.HASH);
	}
	
	public void enviarHashString(String string) throws Exception {
		byte[] info = md.digest(string.getBytes("UTF-8"));
		os.writeInt(info.length);
		os.flush();
		os.write(info);
		os.flush();
	}
	
	public void enviarString(String string) throws Exception {
		os.writeUTF(string);
		os.flush();
	}
	
	public void iniciarConexion() throws Exception{
		if(is.readUTF().equals("Preparado")) {
			enviarString(documento);
			enviarHashString(documento);
			if(!is.readUTF().equals("Recibido")) {
				throw new Exception();
			}
		}
		else {
			throw new Exception();
		}
	}

	public void run() {
		try {
			iniciarConexion();
			socket.close();
			
		} catch (Exception e) {
			try {
				socket.close();
			} catch (IOException e1) {
				System.out.println(e1.getMessage());
				e1.printStackTrace();
			}
			System.out.println(e.getMessage());
			e.printStackTrace();
		}

	}

}
