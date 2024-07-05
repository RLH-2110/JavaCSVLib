package roland.csvlib.exception;

public class DelimeterNotSupportedException extends RuntimeException {
    public DelimeterNotSupportedException(){
        super("Delimeter not supported!");
    }

    DelimeterNotSupportedException(String message){
        super(message);
    }
}
