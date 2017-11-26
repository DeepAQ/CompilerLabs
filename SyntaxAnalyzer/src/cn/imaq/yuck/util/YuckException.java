package cn.imaq.yuck.util;

import java.io.IOException;

public class YuckException extends IOException {
    public YuckException(String message) {
        super(message);
    }

    public YuckException(String message, Throwable cause) {
        super(message, cause);
    }
}
