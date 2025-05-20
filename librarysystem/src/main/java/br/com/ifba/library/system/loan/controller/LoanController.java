package br.com.ifba.library.system.loan.controller;

import br.com.ifba.library.system.loan.dto.LoanDto;
import br.com.ifba.library.system.loan.entity.Loan;
import br.com.ifba.library.system.loan.service.LoanIService;
import br.com.ifba.library.system.mapper.ObjectMapperUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/loans")
@RequiredArgsConstructor
public class LoanController {

    private final LoanIService loanService;
    private final ObjectMapperUtil objectMapperUtil;

    @PostMapping
    public ResponseEntity<LoanDto> createLoan(@Valid @RequestBody LoanDto loanDto) {
        LoanDto savedLoanDto = loanService.newLoan(loanDto);
        return new ResponseEntity<>(savedLoanDto, HttpStatus.CREATED);
    }

    @PatchMapping("/{id}/return")
    public ResponseEntity<Void> returnLoan(@PathVariable Long id, @RequestBody LocalDate returnDate) {
        loanService.returnLoan(id, returnDate);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<LoanDto> getLoanById(@PathVariable Long id) {
        Loan loan = loanService.findById(id);
        LoanDto loanDto = objectMapperUtil.map(loan, LoanDto.class);
        return ResponseEntity.ok(loanDto);
    }

    @GetMapping
    public ResponseEntity<List<LoanDto>> getAllLoans() {
        List<Loan> loans = loanService.findAll();
        List<LoanDto> loanDtos = objectMapperUtil.mapAll(loans, LoanDto.class);
        return ResponseEntity.ok(loanDtos);
    }
}