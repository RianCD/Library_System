package br.com.ifba.library.system.loan.repository;

import br.com.ifba.library.system.loan.entity.Loan;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LoanIRepository extends JpaRepository<Loan, Long> {

}
