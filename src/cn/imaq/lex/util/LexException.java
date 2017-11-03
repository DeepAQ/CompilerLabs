package cn.imaq.lex.util;

import java.io.IOException;

public class LexException extends IOException {
    public LexException(String message) {
        super(message);
    }

    public LexException(String message, Throwable cause) {
        super(message, cause);
    }
}
