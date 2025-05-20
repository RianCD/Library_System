package br.com.ifba.library.system.loan.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * Classe que encapsula detalhes de erros de validação para respostas de API.
 */
@Data
@AllArgsConstructor
@Builder
public class ValidationExceptionDetails {
    /**
     * Data e hora em que o erro ocorreu.
     */
    private LocalDateTime timestamp;

    /**
     * Código de status HTTP associado ao erro (ex.: 400 para Bad Request).
     */
    private int status;

    /**
     * Título descritivo do erro (ex.: "Erro de Validação").
     */
    private String title;

    /**
     * Descrição detalhada do erro.
     */
    private String details;

    /**
     * Mensagem técnica para desenvolvedores (ex.: nome da classe da exceção).
     */
    private String developerMessage;

    /**
     * Nomes dos campos que falharam na validação, separados por vírgulas.
     */
    private String fields;

    /**
     * Mensagens de erro associadas aos campos, separadas por ponto e vírgula.
     */
    private String fieldsMessage;
}