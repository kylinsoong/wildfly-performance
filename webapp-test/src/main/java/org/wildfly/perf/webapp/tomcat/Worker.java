package org.wildfly.perf.webapp.tomcat;

import java.net.Socket;

public class Worker implements Runnable {
	
    protected Thread thread = null;
    protected boolean available = false;
    protected Socket socket = null;
    protected SocketStatus status = null;
    
    private boolean running = true;
    
	protected synchronized void assign(Socket socket) {

		// Wait for the Processor to get the previous Socket
		while (available) {
			try {
				wait();
			} catch (InterruptedException e) {
			}
		}

		// Store the newly available Socket and notify our thread
		this.socket = socket;
		this.status = null;
		available = true;
		notifyAll();

	}
	
	 protected synchronized void assign(Socket socket, SocketStatus status) {

         // Wait for the Processor to get the previous Socket
         while (available) {
             try {
                 wait();
             } catch (InterruptedException e) {
             }
         }

         // Store the newly available Socket and notify our thread
         this.socket = socket;
         this.status = status;
         available = true;
         notifyAll();

     }
	 
	 private synchronized Socket await() {

         // Wait for the Connector to provide a new Socket
         while (!available) {
             try {
                 wait();
             } catch (InterruptedException e) {
             }
         }

         // Notify the Connector that we have received this Socket
         Socket socket = this.socket;
         available = false;
         notifyAll();

         return (socket);

     }
	 
	 public void start() {
         thread = new Thread(this);
//         thread.setName("http-127.0.0.1-0");
         thread.setDaemon(false);
         thread.start();
     }

	public void run() {

		// Process requests until we receive a shutdown signal
		while (running) {

			// Wait for the next socket to be assigned
			Socket socket = await();
			if (socket == null)
				continue;

			// Process the request from this socket
			if (status != null) {

			} else {
				// 1: Set socket options: timeout, linger, etc
				// 2: hander handle socket
				// 3: close socket
			}

		}
	}

}
