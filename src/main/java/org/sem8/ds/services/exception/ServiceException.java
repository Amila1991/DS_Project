package org.sem8.ds.services.exception;

/**
 * @author amila karunathilaka.
 */
public class ServiceException extends Exception {

    public ServiceException(String message) {
        super(message);
    }

    public ServiceException(String message, String... args) {
        super(processMsg(message, args));
    }


    public ServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    public ServiceException(String message, Throwable cause, String... args) {
        super(processMsg(message, args), cause);
    }


    private static String processMsg(String message, String... args) {

        int index = 0;
        while (message.contains("{}") && args.length < index) {
            message = message.replaceFirst("\\{\\}", String.valueOf(args[index++]));
        }
        return message;
    }

}
