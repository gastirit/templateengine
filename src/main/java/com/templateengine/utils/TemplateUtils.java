package com.templateengine.utils;

import groovy.lang.Binding;
import groovy.lang.GroovyShell;
import nu.validator.messages.MessageEmitter;
import nu.validator.messages.TextMessageEmitter;
import nu.validator.servlet.imagereview.ImageCollector;
import nu.validator.source.SourceCode;
import nu.validator.validation.SimpleDocumentValidator;
import nu.validator.xml.SystemErrErrorHandler;
import org.apache.commons.collections.CollectionUtils;
import org.jsoup.nodes.Element;
import org.xml.sax.InputSource;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Collectors;

/**
 * This utils Class handles common processes on templates
 */
public class TemplateUtils {

    private static String url = "http://s.validator.nu/html5-rdfalite.rnc";

    /**
     * readFileAsString
     * @param fileName
     * @return String content of file
     * @throws Exception
     */
    public static String readFileAsString(String fileName)throws Exception
    {
        String data = "";
        data = new String(Files.readAllBytes(Paths.get(fileName)));
        return data;
    }

    /**
     * validateTemplate validates content of template
     * validates contents of XHTML,HTML, HTML5
     * @param content
     * @return TemplateHandler
     * @throws Exception
     */
    public static TemplateHandler validateTemplate(String content) throws Exception {


        TemplateHandler handler = new TemplateHandler();
        InputStream in = new ByteArrayInputStream( content.getBytes( "UTF-8" ));
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        SourceCode sourceCode = new SourceCode();
        ImageCollector imageCollector = new ImageCollector(sourceCode);
        boolean showSource = false;
        MessageEmitter emitter = new TextMessageEmitter( out, false );
        MessageEmitterWrapper errorHandler = new MessageEmitterWrapper(sourceCode, showSource, imageCollector, 0, false, emitter );
        errorHandler.setErrorsOnly(true);

        SimpleDocumentValidator validator = new SimpleDocumentValidator();
        validator.setUpMainSchema(url , new SystemErrErrorHandler());
        validator.setUpValidatorAndParsers(errorHandler, true, false );
        validator.checkHtmlInputSource(new InputSource(in));
        handler.setValid(0 == errorHandler.getErrors());
        if(CollectionUtils.isNotEmpty(errorHandler.getErrorsList())) {
            handler.setMessage(errorHandler.getErrorsList().stream().map(Object::toString).collect(Collectors.joining("\n")));
        }
        return handler;
    }

    /**
     *
     * @param binding
     * @param element
     * @param attribute
     * @return value as Object from expressions of template element's attributes
     */
    public static Object extractValue(Binding binding, Element element, String attribute) {
        String evaluate = element.attr(attribute);
        return extractObject(evaluate, binding);
    }

    /**
     *
     * @param value
     * @param bindingName
     * @param element
     * @param attribute
     * @return value as Object from expressions of template element's attributes
     */
    public static Object extractValue(Object value, String bindingName, Element element, String attribute) {
        String evaluate = element.attr(attribute);
        Binding binding = new Binding();
        binding.setVariable(bindingName, value);
        return extractObject(evaluate, binding);
    }

    /**
     *
     * @param evaluate
     * @param binding
     * @return Object from groovy script
     */
    public static Object extractObject(String evaluate, Binding binding) {
        GroovyShell shell = new GroovyShell(binding);
        return shell.evaluate(evaluate);
    }
}
