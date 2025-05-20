public class NoCorrespondingClient extends Exception{
    
    private final String message;

    public NoCorrespondingClient(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return this.message;
    }
}
