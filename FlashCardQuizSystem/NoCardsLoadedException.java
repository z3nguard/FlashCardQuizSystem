public class NoCardsLoadedException extends RuntimeException {

    public NoCardsLoadedException(String message) {
        super(message);
    }

    public NoCardsLoadedException() {
        super("The flashcard set is empty. Please load a topic or add new cards first.");
    }
}
