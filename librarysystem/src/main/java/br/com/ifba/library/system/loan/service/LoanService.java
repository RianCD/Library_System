package br.com.ifba.library.system.loan.service;

import br.com.ifba.library.system.loan.entity.Loan;
import br.com.ifba.library.system.loan.repository.LoanIRepository;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;

@RequiredArgsConstructor
public class LoanService implements LoanIService{
    private final LoanIRepository loanIRepository;


    @Override
    public Loan newLoan(Loan loan) {
        return null;
    }

    @Override
    public void returnLoan(Long loanId, LocalDate returnDate) {

    }
}
