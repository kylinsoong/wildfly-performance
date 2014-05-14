package com.kylin.webapp.servlet;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

public class SocketAttack {


	public static void main(String[] args) throws UnknownHostException, IOException {

		List<Socket> list = new ArrayList<Socket>();
		
		for(int i = 0 ; i < 100 ; i ++) {
			Socket socket = new Socket("localhost", 8080);
			
			System.out.println("Connect to localhost:8080");
			
			list.add(socket);
		}
		
		System.out.println("list size: " + list.size());
		
		for (int i = 0 ; i < 100 ; i ++) {
			list.get(i).close();
		}
	}

}
