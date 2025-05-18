package br.com.ifba.library.system.loan.entity;

import br.com.ifba.library.system.book.entity.Book;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDate;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
public class Loan {
    @Id
    private Long id;
    @OneToOne
    private Book bookId;

    private Long userId;
    private LocalDate startDate;
    private LocalDate endDate;

}