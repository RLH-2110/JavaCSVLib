package roland.jmethod.exceptions;

/**
 * A general exceptions that combines multiple exceptions while working with types.
 * I should have made it safe, I think, so I hope you never see this one in the console.
 */
public class TypeException extends RuntimeException {
    public TypeException(String message){
        super(message);
    }
    public TypeException(){
        super("Error while working with types!");
    }

    public TypeException(Throwable cause){
        super("Error while working with types!",cause);
    }

    public TypeException(String message, Throwable cause){
        super(message,cause);
    }
}
