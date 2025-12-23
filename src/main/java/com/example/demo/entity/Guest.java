package com.example.demo.servlet;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.annotation.WebServlet;
import java.io.IOException;

@WebServlet(urlPatterns = "/hello-servlet")
public class HelloServlet extends HttpServlet {

    private String message;

    @Override
    public void init() {
        message = "Hello Tomcat";
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setStatus(HttpServletResponse.SC_OK);
        resp.getWriter().write("Hello from servlet");
    }

    public String getMessage() {
        return message;
    }
}
