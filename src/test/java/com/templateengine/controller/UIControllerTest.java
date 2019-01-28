package com.templateengine.controller;


import org.junit.Test;
import org.mockito.Mockito;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import static junit.framework.TestCase.assertEquals;
import static org.mockito.Mockito.verify;


public class UIControllerTest {

    @Test
    public void onGet() throws ServletException, IOException {
        final HttpServletRequest httpServletRequest = Mockito.mock(HttpServletRequest.class);
        final HttpServletResponse httpServletResponse = Mockito.mock(HttpServletResponse.class);
        final ServletContext servletContext = Mockito.mock(ServletContext.class, "getServletContext");
        UIController someServlet = new UIController(){
            public ServletContext getServletContext() {
                return servletContext; // return the mock
            }
        };
        ServletOutputStream outputStream = Mockito.mock(ServletOutputStream.class);

        Mockito.when(httpServletRequest.getRequestURI()).thenReturn("/index.tpl");
        Mockito.when(httpServletRequest.getQueryString()).thenReturn("id=2");
        Mockito.when(httpServletRequest.getServletContext()).thenReturn(servletContext);
        Mockito.when(httpServletRequest.getServletContext().getContext("/index.tpl")).thenReturn(servletContext);
        Mockito.when(httpServletResponse.getOutputStream()).thenReturn(outputStream);
        String yourSystemPath = "add_your_file_path_to_project";  // "C:\\Users\\MSI-PC\\IdeaProjects\\";
        String path = yourSystemPath + "TemplateEngine\\src\\main\\webapp";
        Mockito.when(httpServletRequest.getServletContext().getRealPath("/")).thenReturn(path);
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);

        Mockito.when(httpServletResponse.getWriter())
                .thenReturn(pw);
        Mockito.doReturn("2").when(httpServletRequest).getParameter("id");

        someServlet.doGet(httpServletRequest, httpServletResponse);
        verify(httpServletResponse).setContentType("text/html;charset=UTF-8");
        verify(httpServletResponse).setCharacterEncoding("UTF-8");
        String result = sw.getBuffer().toString().trim();

        String expectedHtml = "<!doctype html>\n" +
                "<html>\n" +
                " <head> \n" +
                "  <title>Mercedes-Benz</title> \n" +
                " </head> \n" +
                " <body> \n" +
                "  <h1 title=\"Mercedes-Benz\">Mercedes-Benz</h1> \n" +
                "  <h2 title=\"Hybrid\">Fuel Type: Hybrid</h2> \n" +
                "  <div>\n" +
                "    Model: Model 0 \n" +
                "  </div> \n" +
                "  <div>\n" +
                "    Model: Model 1 \n" +
                "  </div> \n" +
                "  <div>\n" +
                "    Model: Model 2 \n" +
                "  </div>  \n" +
                " </body>\n" +
                "</html>";

        assertEquals(expectedHtml, result);

    }
}