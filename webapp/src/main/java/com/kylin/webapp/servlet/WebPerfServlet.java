package com.kylin.webapp.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@WebServlet("/WebPerfServlet")
public class WebPerfServlet extends HttpServlet {

  
	private static final long serialVersionUID = 1770304184548479022L;


	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		System.out.println("Web Performance Test Servlet Start " + Thread.currentThread().getName());
		
		try {
			Thread.currentThread().sleep(1000 * 60 * 10);
		} catch (InterruptedException e) {
		}
		
		System.out.println("Web Performance Test Servlet Stop " + Thread.currentThread().getName());
	}

	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doGet(req, resp);
	}

}
