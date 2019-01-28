package com.templateengine.controller;

import com.templateengine.Car;
import com.templateengine.manager.TemplateManager;
import com.templateengine.manager.TemplateManagerImpl;
import com.templateengine.utils.ExceptionUtils;
import com.templateengine.utils.TemplateHandler;
import com.templateengine.utils.TemplateUtils;
import groovy.lang.Binding;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * This controller gets all requests
 * then evaluate template file and processes it as response
 */
public class UIController extends HttpServletHandler {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        Document index = null;
        String message = null;
        TemplateManager manager = new TemplateManagerImpl();
        try {
            String content = manager.getContent(request);
            index = Jsoup.parse(content);
            Elements scripts = index.select("script");
            String groovy = null;
            for (Element script : scripts) {
                if(script.attr("type").equalsIgnoreCase("server/groovy")) {
                    groovy = script != null && script.data() != null && !script.data().isEmpty() ? script.data() : null;
                    if(StringUtils.isNotEmpty(groovy)) {
                        script.remove();
                    }
                }
            }

            Object modelObject = null;
            if(StringUtils.isNotEmpty(groovy)) {
                Binding binding = new Binding();
                binding.setVariable("request", request);
                modelObject = TemplateUtils.extractObject(groovy, binding);
            }

            if(modelObject != null && CollectionUtils.isNotEmpty(index.getAllElements())) {
                manager.processHtmlElements(index.getAllElements(), (Car) modelObject, "car");
            }

            String bodyHtml = manager.execute(index.html(), (Car)modelObject, "car");
            TemplateHandler template = TemplateUtils.validateTemplate(bodyHtml);
            if(template.isValid()) {
                index = Jsoup.parse(bodyHtml);
            } else {
                message = template.getMessage();
            }
        } catch (Exception e) {
            message = ExceptionUtils.getExceptionAsString(e);
        }

        if(index != null) {
            response.setContentType("text/html;charset=UTF-8");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().println(index.html());
        } else {
            throw new ServletException(message);
        }
    }



}
