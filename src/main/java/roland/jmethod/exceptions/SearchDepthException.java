package roland.jmethod.exceptions;

public class SearchDepthException extends RuntimeException {
    public SearchDepthException() {
        super("went over the search depth!");
    }
    public SearchDepthException(String message) {
        super(message);
    }
    public SearchDepthException(int depth) {
        super("went over the search depth of "+depth+'!');
    }
}
