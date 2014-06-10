package org.wildfly.perf.webapp.test;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

public class SimpleClient {

	public static void main(String[] args) throws UnknownHostException, IOException, InterruptedException {
		
		Socket socket = new Socket("127.0.0.1", 9090);
		
//		Thread.currentThread().sleep(1000 * 10);
		
		OutputStream out = socket.getOutputStream();
		out.write("Test String".getBytes());
		out.flush();
		
		byte[] buf = new byte[1024];
		InputStream in = socket.getInputStream();
		in.read(buf);
		System.out.println(new String(buf));
		
		System.out.println(socket.getInetAddress() + " exist");
	}

}
