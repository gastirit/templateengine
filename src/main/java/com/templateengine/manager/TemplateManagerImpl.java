package com.templateengine.manager;

import com.templateengine.utils.TemplateUtils;
import groovy.lang.Writable;
import groovy.text.SimpleTemplateEngine;
import groovy.text.Template;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import java.io.StringWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Template Manager Implementation
 */
public class TemplateManagerImpl implements TemplateManager{
    @Override
    public String execute(String html,Object bindingValue, String bindingName) throws Exception {
        SimpleTemplateEngine engine = new SimpleTemplateEngine();
        Template groovyTemplate = engine.createTemplate(html);
        Map<String, Object> models = new HashMap<>();
        models.put(bindingName, bindingValue);
        Writable output = groovyTemplate.make(models);
        Writer writer = new StringWriter();
        output.writeTo(writer);
        return writer != null ? writer.toString() : null;
    }

    @Override
    public String getContent(HttpServletRequest request) throws Exception{
        String path = request.getRequestURI();
        ServletContext ctx = request.getServletContext().getContext(path);
        String homeFile = ctx.getRealPath("/") + path;
        return TemplateUtils.readFileAsString(homeFile);
    }

    @Override
    public void processHtmlElements(Elements allElements, Object modelObject, String bindingName) throws Exception{
        for(Element element : allElements) {
            if(element.hasAttr("data-if")) {
                Object value = TemplateUtils.extractValue(modelObject, bindingName, element, "data-if");
                AtomicBoolean visibility = new AtomicBoolean(false);
                visibility.set(((Boolean) value).booleanValue());

                if(!visibility.get()) {
                    element.attr("style", "display:none");
                }
                element.removeAttr("data-if");
            }

            if(element.tagName().equals("div")) {
                String loopName = null;
                for(Attribute attribute : element.attributes()) {
                    if(attribute.getKey().contains("data-loop-")) {
                        loopName = attribute.getKey().replace("data-loop-","");
                    }
                }

                if(loopName != null) {
                    String dataLoop = "data-loop-" + loopName;
                    Object value = TemplateUtils.extractValue(modelObject, bindingName, element, dataLoop);
                    List<Object> bindingList = ((List<Object>) value);

                    element.removeAttr(dataLoop);

                    if(CollectionUtils.isEmpty(bindingList)) {
                        element.empty();
                        element.text("There is no model defined.");
                    } else {
                        for(Object bind : bindingList) {
                            String html = execute(element.outerHtml(), (Object)bind, loopName);
                            element.before(StringUtils.isNotEmpty(html) ? html : StringUtils.EMPTY);
                        }
                        element.remove();
                    }
                }

            }
        }
    }


}
