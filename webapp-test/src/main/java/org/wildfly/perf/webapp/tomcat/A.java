package org.wildfly.perf.webapp.tomcat;

public class A {
	
	public void foo() {
		
		synchronized(this) {
			System.out.println("foo start");
			
			try {
				wait();
			} catch (InterruptedException e) {
			}
			
			System.out.println("foo end");
		}
	}
	
	public void zoo() {
		synchronized (this) {
			notifyAll();
//			notify();
		}
	}
	

}
