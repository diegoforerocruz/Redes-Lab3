package paq;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.net.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Server {

	public final static String IP = "127.0.0.1";
	public final static String HASH = "SHA-256";
	public final static int PUERTO = 5000;

	private BufferedReader consola;
	private ExecutorService pool;
	private ServerSocket server;
	private static BufferedWriter bw;
	private String documento;
	private Socket socket;

	public Server() throws Exception {
		inicializarServidor();
		imprimir("Servidor Iniciado");
		while (true) {
			imprimir("Esperando conexion ...");
			socket = server.accept();
			pool.execute(new ThreadServidor(socket, documento));
			imprimir("Cliente aceptado");
			break;
		}
		pool.shutdown();
		pool.awaitTermination(5, TimeUnit.SECONDS);
		consola.close();
		bw.close();
		server.close();
	}

	public void inicializarServidor() throws Exception{
		File log = new File("./Files/log.txt");
		if(!log.exists()) {
			log.createNewFile();
		}
		bw = new BufferedWriter(new FileWriter(log));
		server = new ServerSocket(PUERTO);
		consola = new BufferedReader(new InputStreamReader(System.in));
		leerArchivoTxt();
		pool = Executors.newFixedThreadPool(25);
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
	
	public static synchronized void imprimir(String string) {
		System.out.println(string);
	}
	
	public static synchronized void imprimirLog(String string) throws Exception{
		bw.write(string +"\n");
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
