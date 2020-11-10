package livraria.service.exceptions;

public class LivroValidationException extends RuntimeException {

    public LivroValidationException(String mensagem) {
        super(mensagem);
    }
}
