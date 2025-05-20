package br.com.ifba.library.system.loan.service;

import br.com.ifba.library.system.loan.dto.LoanDto;
import br.com.ifba.library.system.loan.entity.Loan;

import java.time.LocalDate;
import java.util.List;

public interface LoanIService {
    LoanDto newLoan(LoanDto loanDto);
    void returnLoan(Long loanId, LocalDate returnDate);
    Loan findById(Long id);
    List<Loan> findAll();
}
