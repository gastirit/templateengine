package com.templateengine.manager;

import org.jsoup.select.Elements;

import javax.servlet.http.HttpServletRequest;

/**
 * TemplateManager Interface is a contract for this service.
 *
 */
public interface TemplateManager {

    /**
     *
     * @param html
     * @param bindingValue
     * @param bindingName
     * @return String of executed template
     * @throws Exception
     */
    String execute(String html, Object bindingValue, String bindingName) throws Exception;

    /**
     *
     * @param request
     * @return String of content of requested path's file
     * @throws Exception
     */
    String getContent(HttpServletRequest request) throws Exception;

    /**
     *  This method will process all Elements of html content
     *  acording to templating logic.
     * @param allElements
     * @param model
     * @param bindingName
     * @throws Exception
     */
    void processHtmlElements(Elements allElements, Object model, String bindingName) throws Exception;
}
