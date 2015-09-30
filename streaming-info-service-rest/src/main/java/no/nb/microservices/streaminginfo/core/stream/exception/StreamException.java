package no.nb.microservices.streaminginfo.core.stream.exception;

/**
 * Created by andreasb on 30.09.15.
 */
public class StreamException extends RuntimeException {
    public StreamException(String message) {
        super(message);
    }

    public StreamException(String message, Throwable cause) {
        super(message, cause);
    }
}
