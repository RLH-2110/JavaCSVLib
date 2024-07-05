package roland.jmethod.exceptions;

/**
 * Runtime Exception for InvocationTargetException
 * this exception is a wrapper for an actual exception that is in the cause.
 */
public class MethodException extends RuntimeException{
    public MethodException(Throwable cause){
        super("Method thew an exception -> "+cause.getMessage(),cause);
    }
}
