package lima.rocha.contadordefaltas;

public class DuplicateKeyException extends Exception {
    public DuplicateKeyException(String message, Throwable cause){
        super(message, cause);
    }
}
