package roland.jmethod.exceptions;

public class MethodHolderNotAttachedException extends RuntimeException{
    public MethodHolderNotAttachedException() {
        super("MethodHolder not attached");
    }
    public MethodHolderNotAttachedException(String message) {
        super(message);
    }
}
