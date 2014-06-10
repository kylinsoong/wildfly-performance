package org.wildfly.perf.webapp.test;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;

public class SimpleServer {

	public static void main(String[] args) throws Exception {

		ServerSocket listener = new ServerSocket(9090);
		System.out.println("Server Socket Start on " + listener.getInetAddress().getLocalHost().getHostName() + File.pathSeparator + listener.getLocalPort() );
		
		while(true) {
			try {
				Socket socket = listener.accept();
				System.out.println("A socket connected, " + socket.getRemoteSocketAddress());
				socket.setSoTimeout(1000 * 5);
				Thread.currentThread().sleep(1000 * 6);
				
				byte[] buf = new byte[1024];
				InputStream in = socket.getInputStream();
				in.read(buf);
				System.out.println(new String(buf));
				
				OutputStream out = socket.getOutputStream();
				out.write("Success".getBytes());
				out.flush();
				
				
			} catch (SocketTimeoutException e) {
				System.out.println("Socket Timeout received: " + e.getMessage());
			}
		}

	}

}
