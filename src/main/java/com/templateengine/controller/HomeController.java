package com.templateengine.controller;


import com.templateengine.utils.ExceptionUtils;
import com.templateengine.utils.TemplateHandler;
import com.templateengine.utils.TemplateUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * This controller handles root request of our application
 */
public class HomeController extends HttpServletHandler {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        Document index = null;
        String message = null;
        try {
            String homeFile = getServletContext().getRealPath("/") + "/" + "home" + EXT;
            String content = TemplateUtils.readFileAsString(homeFile);
            TemplateHandler template = TemplateUtils.validateTemplate(content);
            if(template.isValid()) {
                index = Jsoup.parse(content);
            } else {
                message = template.getMessage();
            }
        } catch (Exception e) {
            response.setContentType("text/html");
            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().println("<h1>White Label Error</h1>");
            response.getWriter().println("Parse Exception: Validate home" + EXT);
            response.getWriter().println(ExceptionUtils.getExceptionAsString(e));
        }

        response.setContentType("text/html;charset=UTF-8");
        response.setCharacterEncoding("UTF-8");

        if(index != null) {
            response.getWriter().println(index.html());
        } else {
            throw new ServletException(message);
        }
    }
}
