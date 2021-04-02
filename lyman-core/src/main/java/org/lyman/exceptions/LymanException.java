package org.lyman.exceptions;

public class LymanException extends RuntimeException {

    private int code;

    public LymanException(int code) {
        super();
        this.code = code;
    }

    public LymanException(int code, String message) {
        super(message);
        this.code = code;
    }

    public LymanException(int code, Throwable cause) {
        super(cause);
        this.code = code;
    }

    public LymanException(int code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
    }

    public int getCode() {
        return this.code;
    }

}
