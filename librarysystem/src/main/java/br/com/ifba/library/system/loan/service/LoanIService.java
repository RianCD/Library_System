package br.com.ifba.library.system.loan.service;

import br.com.ifba.library.system.loan.entity.Loan;

import java.time.LocalDate;

public interface LoanIService {
    Loan newLoan(Loan loan);
    void returnLoan(Long loanId, LocalDate returnDate);

}
