package roland.jmethod.exceptions;

public class ClassSetterCantParseStringException extends RuntimeException {
    public ClassSetterCantParseStringException(String message){
        super(message);
    }
    public ClassSetterCantParseStringException(){
        super("StringAccessMethodHolder cant work with class setters!");
    }

    public ClassSetterCantParseStringException(Throwable cause){
        super("StringAccessMethodHolder cant work with class setters!",cause);
    }

    public ClassSetterCantParseStringException(String message, Throwable cause){
        super(message,cause);
    }
}
