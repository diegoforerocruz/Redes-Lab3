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
		StringBuilder concatenador = new StringBuilder("Tiempo de envio del documento con hash: ");
		if(is.readUTF().equals("Preparado")) {
			Server.imprimir("Enviando documento");
			long tiempo = System.currentTimeMillis();
			enviarString(documento);
			Server.imprimir("Enviando hash documento");
			enviarHashString(documento);
			if(!is.readUTF().equals("Recibido")) {
				tiempo = System.currentTimeMillis() - tiempo;
				Server.imprimirLog(concatenador.append(tiempo).append("ms. El estado del envio fue incompleto").toString());
				throw new Exception();
			}
			tiempo = System.currentTimeMillis() - tiempo;
			Server.imprimirLog(concatenador.append(tiempo).append("ms. El estado del envio fue completo").toString());
			Server.imprimir("Comprobante recibido");
		}
		else {
			throw new Exception();
		}
	}

	public void run() {
		try {
			iniciarConexion();
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
