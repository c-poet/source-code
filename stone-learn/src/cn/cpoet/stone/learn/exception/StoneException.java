package cn.cpoet.stone.learn.exception;

/**
 * @author CPoet
 */
public class StoneException extends RuntimeException {
    public StoneException(String message) {
        super(message);
    }

    public StoneException(String message, Throwable cause) {
        super(message, cause);
    }

    public StoneException(Throwable cause) {
        super(cause);
    }
}
