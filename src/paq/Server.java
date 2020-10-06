package paq;

import java.io.*;
import java.net.*;
import java.security.MessageDigest;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {

	public final static String PRUEBA = "./Files/prueba.txt";
	public final static String SHREK = "./Files/Shrek1-2Script.txt";
	public final static String INTERSTELLAR = "./Files/Interstellar-script.txt";
	public final static String IP = "127.0.0.1";
	public final static String HASH = "SHA-256";
	public final static int PUERTO = 5000;

	private static String interstellar;
	private static BufferedWriter bw;
	private static String prueba;
	private static String shrek;

	static File log = new File("./Files/log.txt");
	private ExecutorService pool;
	private ServerSocket server;
	private Socket socket;

	public Server() throws Exception {
		inicializarServidor();
		imprimir("Servidor Iniciado");
		while (true) {
			imprimir("Esperando conexion ...");
			socket = server.accept();
			pool.execute(new ThreadServidor(socket));
			imprimir("Cliente aceptado");
			break;
		}
		pool.shutdown();
	}

	public void inicializarServidor() throws Exception {
		if (!log.exists()) {
			log.createNewFile();
		}
		bw = new BufferedWriter(new FileWriter(log));
		server = new ServerSocket(PUERTO);
		prueba = leerArchivoTxt(PRUEBA);
		shrek = leerArchivoTxt(SHREK);
		interstellar = leerArchivoTxt(INTERSTELLAR);
		pool = Executors.newFixedThreadPool(25);
	}

	public String leerArchivoTxt(String direccion) throws Exception {
		BufferedReader br = new BufferedReader(new FileReader(new File(direccion)));
		String next = br.readLine();
		StringBuilder concatenador = new StringBuilder(next);
		int i = 0;
		while (next != null) {
			if (i != 0) {
				concatenador.append("\n").append(next);
			}
			next = br.readLine();
			i++;
		}
		br.close();
		return concatenador.toString();
	}

	public static String darPrueba() {
		return prueba;
	}

	public static String darShrek() {
		return shrek;
	}

	public static String darInterstellar() {
		return interstellar;
	}

	public static synchronized void imprimir(String string) {
		System.out.println(string);
	}

	public static synchronized void imprimirLog(String string) throws Exception {
		bw.write(string + "\n");
		bw.close();
		bw = new BufferedWriter(new FileWriter(log, true));
	}

	public static void main(String args[]) {
		try {
			new Server();
		} catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
	}

	class ThreadServidor implements Runnable {

		private Socket socket;
		private DataOutputStream os;
		private DataInputStream is;
		private ObjectOutputStream bw;
		private MessageDigest md;

		public ThreadServidor(Socket socket) throws Exception {
			this.socket = socket;
			os = new DataOutputStream(socket.getOutputStream());
			is = new DataInputStream(socket.getInputStream());
			bw = new ObjectOutputStream(socket.getOutputStream());
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
			bw.writeObject(string);
			bw.flush();
		}

		public void enviarArchivo() throws Exception {
			String peticion = is.readUTF();
			if (peticion.equals("250mb")) {
				enviarShrek();
			} else if (peticion.equals("100mb")) {
				enviarInterstellar();
			}
		}

		public void iniciarConexion() throws Exception {
			StringBuilder concatenador = new StringBuilder("Tiempo de envio del documento de prueba: ");
			if (is.readUTF().equals("Preparado")) {
				Server.imprimir("Enviando documento");
				long tiempo = System.currentTimeMillis();
				enviarString(Server.darPrueba());
				enviarHashString(Server.darPrueba());
				if (!is.readUTF().equals("Recibido")) {
					tiempo = System.currentTimeMillis() - tiempo;
					Server.imprimirLog(
							concatenador.append(tiempo).append("ms. El estado del envio fue incompleto").toString());
					throw new Exception();
				}
				tiempo = System.currentTimeMillis() - tiempo;
				Server.imprimirLog(
						concatenador.append(tiempo).append("ms. El estado del envio fue completo").toString());
				Server.imprimir("Comprobante recibido");
			} else {
				throw new Exception();
			}
		}

		public void enviarInterstellar() throws Exception {
			Server.imprimir("Enviando documento");
			StringBuilder concatenador = new StringBuilder("Tiempo de envio del documento de 100mb: ");
			long tiempo = System.currentTimeMillis();
			enviarString(Server.darInterstellar());
			enviarHashString(Server.darInterstellar());
			if (!is.readUTF().equals("Recibido")) {
				tiempo = System.currentTimeMillis() - tiempo;
				Server.imprimirLog(
						concatenador.append(tiempo).append("ms. El estado del envio fue incompleto").toString());
				throw new Exception();
			}
			tiempo = System.currentTimeMillis() - tiempo;
			Server.imprimirLog(concatenador.append(tiempo).append("ms. El estado del envio fue completo").toString());
			Server.imprimir("Comprobante recibido");
		}

		public void enviarShrek() throws Exception {
			Server.imprimir("Enviando documento");
			StringBuilder concatenador = new StringBuilder("Tiempo de envio del documento de 250mb: ");
			long tiempo = System.currentTimeMillis();
			enviarString(Server.darShrek());
			enviarHashString(Server.darShrek());
			if (!is.readUTF().equals("Recibido")) {
				tiempo = System.currentTimeMillis() - tiempo;
				Server.imprimirLog(
						concatenador.append(tiempo).append("ms. El estado del envio fue incompleto").toString());
				throw new Exception();
			}
			tiempo = System.currentTimeMillis() - tiempo;
			Server.imprimirLog(concatenador.append(tiempo).append("ms. El estado del envio fue completo").toString());
			Server.imprimir("Comprobante recibido");
		}

		public void run() {
			try {
				iniciarConexion();
				enviarArchivo();
				os.close();
				is.close();
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

}
