package org.wildfly.perf.webapp.tomcat;

public class ObjectWaitTest {

	public static void main(String[] args) throws InterruptedException {
		
		final A a = new A();

		new Thread(new Runnable(){

			public void run() {
				a.foo();
			}}).start();
		
		new Thread(new Runnable(){

			public void run() {
				a.foo();
			}}).start();
		
		new Thread(new Runnable(){

			public void run() {
				a.zoo();
			}}).start();
		
		
	}


}

