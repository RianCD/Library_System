package br.com.ifba.library.system.book.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookDto {
    @NotNull(message = "O ID do livro é obrigatório")
    @Positive(message = "O ID deve ser um número positivo")
    private Long id;

    @NotBlank(message = "O título é obrigatório")
    @Size(max = 255, message = "O título não pode exceder 255 caracteres")
    private String title;

    @NotBlank(message = "O autor é obrigatório")
    @Size(max = 100, message = "O autor não pode exceder 100 caracteres")
    private String author;

    @PastOrPresent(message = "A data de publicação não pode ser futura")
    private LocalDate publishedDate;
}
