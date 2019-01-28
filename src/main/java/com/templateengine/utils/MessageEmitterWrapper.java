package com.templateengine.utils;

import nu.validator.messages.MessageEmitter;
import nu.validator.messages.MessageEmitterAdapter;
import nu.validator.servlet.imagereview.ImageCollector;
import nu.validator.source.SourceCode;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import java.util.LinkedList;

/**
 * This Wrapper extends MessageEmitterAdapter
 * and collects syntax errors of templates
 */
public class MessageEmitterWrapper extends MessageEmitterAdapter implements ErrorHandler {
    private boolean fatal = false;

    private LinkedList<String> errorsList = new LinkedList<String>();

    public MessageEmitterWrapper(SourceCode sourceCode, boolean showSource, ImageCollector imageCollector, int i, boolean b, MessageEmitter emitter) {
        super(null, sourceCode, showSource, imageCollector, 0, false, emitter );
    }

    public void error(SAXParseException spe) throws SAXException {
        errorsList.add(Integer.toString(spe.getColumnNumber()) + ": " + spe.getMessage());
        super.error(spe);
    }

    public void fatalError(SAXParseException spe) throws SAXException {
        fatal = true;
        super.fatalError(spe);
    }

    public void warning(SAXParseException spe) throws SAXException {
        super.warning(spe);
    }

    /**
     * Returns the errors.
     *
     * @return the errors
     */
    public LinkedList<String> getErrorsList() {
        return errorsList;
    }

    /**
     * Returns the fatal.
     *
     * @return the fatal
     */
    public boolean isFatal() {
        return fatal;
    }
}
