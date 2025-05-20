package br.com.ifba.library.system.loan.exception;

import lombok.Getter;

/**
 * Exceção personalizada para erros de validação em operações de empréstimo.
 */
@Getter
public class LoanValidationException extends RuntimeException {
    /**
     * Detalhes do erro de validação.
     */
    private final ValidationExceptionDetails details;

    /**
     * Construtor que inicializa a exceção com detalhes de validação.
     *
     * @param details Objeto contendo informações sobre o erro.
     */
    public LoanValidationException(ValidationExceptionDetails details) {
        super(details.getDetails());
        this.details = details;
    }

    /**
     * Construtor que inicializa a exceção com uma mensagem e detalhes de validação.
     *
     * @param message Mensagem descritiva do erro.
     * @param details Objeto contendo informações sobre o erro.
     */
    public LoanValidationException(String message, ValidationExceptionDetails details) {
        super(message);
        this.details = details;
    }
}
