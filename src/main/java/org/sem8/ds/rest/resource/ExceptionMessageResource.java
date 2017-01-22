package org.sem8.ds.rest.resource;

/**
 * @author amila karunathilaka.
 */
public class ExceptionMessageResource {

    private String message;

    public ExceptionMessageResource(String message) {
        this.message = message;
    }

    public ExceptionMessageResource(String message, String... args) {
        this.message = processMsg(message, args);
    }

    private String processMsg(String message, String... args) {
        int index = 0;
        while (message.contains("{}") && args.length < index) {
            message = message.replaceFirst("\\{\\}", String.valueOf(args[index++]));
        }
        return message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
