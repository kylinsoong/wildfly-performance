package org.wildfly.perf.webapp.tomcat;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class Acceptor implements Runnable {
	
	

	public void run() {
		
		ServerSocket serverSocket = null ;
		
		try {
			serverSocket = new ServerSocket(8080, 100, InetAddress.getByName("127.0.0.1"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		while(true) {
			
			try {
				Socket socket = serverSocket.accept();
				
				if (!processSocket(socket)) {
                    // Close socket right away
                    try { socket.close(); } catch (IOException e) { }
                }
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	private boolean processSocket(Socket socket) {
		
		Worker workerThread = new Worker();
		workerThread.start();
		
		if (workerThread != null) {
			workerThread.assign(socket);
        } else {
            return false;
        }

		return true;
	}

}
