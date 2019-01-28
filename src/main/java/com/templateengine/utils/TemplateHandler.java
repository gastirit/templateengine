package com.templateengine.utils;

/**
 * This class keeps template execution state
 * if is valid or not
 * Message is filled when is not valid template
 */
public class TemplateHandler {

    private String message;
    private boolean valid;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + "{" +
                "message='" + message + '\'' +
                ", valid=" + valid +
                '}';
    }
}
