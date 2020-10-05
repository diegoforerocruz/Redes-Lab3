package paq;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.net.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {

	public final static String IP = "127.0.0.1";
	public final static String HASH = "SHA-256";
	public final static int PUERTO = 5000;

	private BufferedReader consola;
	private ServerSocket server;
	private String documento;
	private Socket socket;

	public Server() throws Exception {
		server = new ServerSocket(PUERTO);
		consola = new BufferedReader(new InputStreamReader(System.in));
		leerArchivoTxt();
		ExecutorService pool = Executors.newFixedThreadPool(25);
		System.out.println("Servidor Iniciado");
		while (true) {
			System.out.println("Esperando conexión ...");
			socket = server.accept();
			pool.execute(new ThreadServidor(socket, documento));
			System.out.println("Cliente aceptado");
		}
	}

	public void leerArchivoTxt() throws Exception {
		BufferedReader br = new BufferedReader(new FileReader(new File("./Files/prueba.txt")));
		String next = br.readLine();
		StringBuilder concatenador = new StringBuilder(next);
		int i = 0;
		while (next != null) {
			if(i!=0) {
				concatenador.append("\n").append(next);
			}
			next = br.readLine();
			i++;
		}
		br.close();
		documento = concatenador.toString();
	}

	public static void main(String args[]) {
		try {
			new Server();
		} catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
	}
}
