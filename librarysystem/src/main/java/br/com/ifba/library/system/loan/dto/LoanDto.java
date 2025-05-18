package br.com.ifba.library.system.loan.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoanDto {
    @NotNull(message = "O ID do empréstimo é obrigatório")
    @Positive(message = "O ID deve ser um número positivo")
    private Long id;

    @NotNull(message = "O ID do livro é obrigatório")
    @Positive(message = "O ID do livro deve ser um número positivo")
    private Long bookId;

    @NotNull(message = "O ID do usuário é obrigatório")
    @Positive(message = "O ID do usuário deve ser um número positivo")
    private Long userId;

    @NotNull(message = "A data de início é obrigatória")
    @PastOrPresent(message = "A data de início não pode ser futura")
    private LocalDate startDate;

    @FutureOrPresent(message = "A data de devolução deve ser hoje ou no futuro")
    private LocalDate endDate;
}
