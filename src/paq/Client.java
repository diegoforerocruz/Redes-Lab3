package paq;

import java.net.*;
import java.security.MessageDigest;
import java.util.Arrays;
import java.io.*;

public class Client {

	private DataOutputStream os;
	private DataInputStream is;
	private MessageDigest md;
	private Socket socket;

	public Client() throws Exception{
			iniciarConexion();
			os.close();
			is.close();
			socket.close();
	}

	public void iniciarConexion() throws Exception{
		socket = new Socket(Server.IP, Server.PUERTO);
		os = new DataOutputStream(socket.getOutputStream());
		is = new DataInputStream(socket.getInputStream());
		md = MessageDigest.getInstance(Server.HASH);
		System.out.println("Conectado");
		os.writeUTF("Preparado");
		System.out.println("Preparado para recibir la respuesta del servidor");
		String documento = is.readUTF();
		int size = is.readInt();
		byte[] hash = new byte[size];
		for(int i=0;i<size;i++) {
			hash[i] = is.readByte();
		}
		if(verificacionHashString(documento, hash)) {
			os.writeUTF("Recibido");
			System.out.println("Mensaje verificado");
		}
	}
	
	public boolean verificacionHashString(String string, byte[] hash) throws Exception {
		if(Arrays.equals(md.digest(string.getBytes("UTF-8")), hash)) {
			return true;
		}
		return false;
	}
	
	public static void main(String args[]) {
		try {
			new Client();

		}
		catch(Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();	
		}
	}
}
