package com.me.tomcat.servlet;


import com.me.tomcat.http.Request;
import com.me.tomcat.http.Response;
import com.me.tomcat.http.Servlet;

public class FirstServlet extends Servlet {

	public void doGet(Request request, Response response) throws Exception {
		this.doPost(request, response);
	}

	public void doPost(Request request, Response response) throws Exception {
		response.write("This is First Serlvet");
	}

}
