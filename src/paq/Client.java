package paq;

import java.net.*;
import java.security.MessageDigest;
import java.util.Arrays;
import java.io.*;

public class Client {

	private DataOutputStream os;
	private DataInputStream is;
	private ObjectInputStream br;
	private MessageDigest md;
	private Socket socket;

	public Client() throws Exception {
		iniciarConexion();
	}

	public void iniciarConexion() throws Exception {
		socket = new Socket(Server.IP, Server.PUERTO);
		os = new DataOutputStream(socket.getOutputStream());
		is = new DataInputStream(socket.getInputStream());
		br = new ObjectInputStream(socket.getInputStream());
		md = MessageDigest.getInstance(Server.HASH);
		System.out.println("Conectado");
		os.writeUTF("Preparado");
		System.out.println("Preparado para recibir la respuesta del servidor");
		pedirArchivo();
	}

	public void pedirShrek() throws Exception {
		System.out.println("Enviando petición de Shrek");
		os.writeUTF("250mb");
		os.flush();
		pedirArchivo();
	}

	public void pedirInterstellar() throws Exception {
		System.out.println("Enviando petición de Interstellar");
		os.writeUTF("100mb");
		os.flush();
		pedirArchivo();
	}

	public String pedirArchivo() throws Exception {
		String documento = (String) br.readObject();
		int size = is.readInt();
		byte[] hash = new byte[size];
		for (int i = 0; i < size; i++) {
			hash[i] = is.readByte();
		}
		if (verificacionHashString(documento, hash)) {
			os.writeUTF("Recibido");
			System.out.println("Mensaje verificado");
		}
		return documento;
	}

	public boolean verificacionHashString(String string, byte[] hash) throws Exception {
		if (Arrays.equals(md.digest(string.getBytes("UTF-8")), hash)) {
			return true;
		}
		return false;
	}

	public static void main(String args[]) {
		try {
			new Client();
			
		} catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
	}
}
