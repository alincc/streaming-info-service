package no.nb.microservices.streaminginfo.core.stream.exception;

/**
 * Created by andreasb on 30.09.15.
 */
public class NoAccessException extends RuntimeException {
    public NoAccessException(String message) {
        super(message);
    }

    public NoAccessException(String message, Throwable cause) {
        super(message, cause);
    }
}
