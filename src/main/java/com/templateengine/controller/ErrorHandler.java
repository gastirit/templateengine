package com.templateengine.controller;

import com.templateengine.utils.ExceptionUtils;
import com.templateengine.utils.TemplateUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 *  Error Handler
 *  This class is responsible to navigate all error
 *  to the error page. Navigation rules are configured in web.xml
 */

@WebServlet("/error")
public class ErrorHandler extends HttpServlet {

    private static final long serialVersionUID = 1L;

    /***** This Method Is Called By The Servlet Container To Process A 'GET' Request *****/
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        handleRequest(request, response);
    }

    /**
     * This method handles process of request and response
     * @param request
     * @param response
     * @throws IOException
     * @throws ServletException
     */
    public void handleRequest(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

        try {
            ServletException exception = (ServletException) request.getAttribute("javax.servlet.error.exception");

            String errorTmp = getServletContext().getRealPath("/") + "/" + "error.tpl";
            String errorContent = TemplateUtils.readFileAsString(errorTmp);
            Document content = Jsoup.parse(errorContent);
            Element title = content.select("p#error-message").first();
            title.text(exception.getCause().getMessage());
            response.getWriter().println(content.html());
        } catch (Exception e) {
            response.setContentType("text/html");
            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().println("<h1>White Label Error</h1>");
            response.getWriter().println("Parse Exception: Validate error.tpl");
            response.getWriter().println(ExceptionUtils.getExceptionAsString(e));
        }
    }
}