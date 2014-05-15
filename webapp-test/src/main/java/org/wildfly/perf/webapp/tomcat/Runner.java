package org.wildfly.perf.webapp.tomcat;

public class Runner {

	public static void main(String[] args) {

		Worker workerThread = new Worker();
		workerThread.start();
		System.out.println("DONE");
	}

}
