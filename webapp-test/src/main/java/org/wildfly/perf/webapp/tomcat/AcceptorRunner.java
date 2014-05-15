package org.wildfly.perf.webapp.tomcat;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class AcceptorRunner {

	public static void main(String[] args) throws UnknownHostException {

		InetAddress address = InetAddress.getByName("127.0.0.1");
		
		System.out.println(address);
	}

}
