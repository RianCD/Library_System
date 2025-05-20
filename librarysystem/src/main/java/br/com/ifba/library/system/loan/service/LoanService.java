package br.com.ifba.library.system.loan.service;

import br.com.ifba.library.system.book.entity.Book;
import br.com.ifba.library.system.book.repository.BookIRepository;
import br.com.ifba.library.system.loan.dto.LoanDto;
import br.com.ifba.library.system.loan.entity.Loan;
import br.com.ifba.library.system.loan.repository.LoanIRepository;
import br.com.ifba.library.system.mapper.ObjectMapperUtil;
import br.com.ifba.library.system.loan.exception.LoanValidationException;
import br.com.ifba.library.system.loan.exception.ValidationExceptionDetails;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class LoanService implements LoanIService {

    private final LoanIRepository loanIRepository;
    private final BookIRepository bookIRepository;
    private final ObjectMapperUtil objectMapperUtil;

    @Override
    public LoanDto newLoan(LoanDto loanDto) {
        log.info("Tentando criar novo empréstimo: {}", loanDto);

        // Validação do DTO (anotações tratadas por @Valid no controlador)
        if (loanDto == null) {
            log.error("LoanDto é nulo");
            throw buildValidationException("LoanDto não pode ser nulo", "loanDto", "Não pode ser nulo");
        }
        if (loanDto.getStartDate() != null && loanDto.getEndDate() != null &&
                !loanDto.getEndDate().isAfter(loanDto.getStartDate())) {
            log.error("Data de devolução {} não é após data de início {}", loanDto.getEndDate(), loanDto.getStartDate());
            throw buildValidationException(
                    "Data de devolução prevista deve ser após a data de início",
                    "endDate",
                    "Deve ser após a data de início"
            );
        }
        if (loanDto.getUserId() == null || loanDto.getUserId() <= 0) {
            log.error("ID do usuário inválido: {}", loanDto.getUserId());
            throw buildValidationException(
                    "ID do usuário deve ser positivo",
                    "userId",
                    "Deve ser positivo"
            );
        }

        // Verifica se o livro existe
        Book book = bookIRepository.findById(loanDto.getBookId())
                .orElseThrow(() -> {
                    log.error("Livro não encontrado para ID: {}", loanDto.getBookId());
                    return new EntityNotFoundException("Livro não encontrado");
                });

        // Verifica se o livro está disponível
        if (loanIRepository.findAll().stream()
                .anyMatch(loan -> loan.getBookId().getId().equals(loanDto.getBookId()) &&
                        (loan.getEndDate().isAfter(LocalDate.now()) || loan.getEndDate().isEqual(LocalDate.now())))) {
            log.error("Livro com ID {} já está emprestado", loanDto.getBookId());
            throw buildValidationException(
                    "Livro já está emprestado",
                    "bookId",
                    "Livro não disponível para empréstimo"
            );
        }

        // Mapeia DTO para entidade
        Loan loan = objectMapperUtil.map(loanDto, Loan.class);
        loan.setBookId(book);
        loan.setUserId(loanDto.getUserId());

        // Define padrões
        if (loan.getStartDate() == null) {
            loan.setStartDate(LocalDate.now());
        }
        if (loan.getEndDate() == null) {
            loan.setEndDate(loan.getStartDate().plusDays(14));
        }

        // Valida a entidade
        if (loan.getBookId() == null) {
            log.error("Livro é nulo");
            throw buildValidationException("Livro é obrigatório", "bookId", "Não pode ser nulo");
        }
        if (loan.getUserId() == null || loan.getUserId() <= 0) {
            log.error("Usuário inválido: {}", loan.getUserId());
            throw buildValidationException("ID do usuário é obrigatório", "userId", "Deve ser positivo");
        }
        if (loan.getStartDate() == null || loan.getStartDate().isAfter(LocalDate.now())) {
            log.error("Data de início inválida: {}", loan.getStartDate());
            throw buildValidationException("Data de início deve ser hoje ou no passado", "startDate", "Data inválida");
        }
        if (loan.getEndDate() == null || !loan.getEndDate().isAfter(loan.getStartDate())) {
            log.error("Data de devolução inválida: {}", loan.getEndDate());
            throw buildValidationException(
                    "Data de devolução prevista deve ser após a data de início",
                    "endDate",
                    "Deve ser após a data de início"
            );
        }

        try {
            Loan savedLoan = loanIRepository.save(loan);
            log.info("Empréstimo criado com sucesso: {}", savedLoan);
            return objectMapperUtil.map(savedLoan, LoanDto.class);
        } catch (Exception e) {
            log.error("Falha ao criar empréstimo: {}", e.getMessage());
            throw new RuntimeException("Erro ao criar empréstimo", e);
        }
    }

    @Override
    public void returnLoan(Long loanId, LocalDate returnDate) {
        log.info("Tentando devolver empréstimo com ID: {}", loanId);

        // Validação
        if (loanId == null || loanId <= 0) {
            log.error("ID do empréstimo inválido: {}", loanId);
            throw buildValidationException("ID do empréstimo deve ser positivo", "loanId", "Deve ser positivo");
        }
        if (returnDate == null || returnDate.isAfter(LocalDate.now())) {
            log.error("Data de devolução inválida: {}", returnDate);
            throw buildValidationException(
                    "Data de devolução deve ser hoje ou no passado",
                    "returnDate",
                    "Deve ser hoje ou no passado"
            );
        }

        // Verifica se o empréstimo existe
        Loan loan = loanIRepository.findById(loanId)
                .orElseThrow(() -> {
                    log.error("Empréstimo não encontrado para ID: {}", loanId);
                    return new EntityNotFoundException("Empréstimo não encontrado");
                });

        // Verifica se o empréstimo está ativo
        if (loan.getEndDate().isBefore(LocalDate.now())) {
            log.error("Empréstimo com ID {} já foi devolvido (endDate: {})", loanId, loan.getEndDate());
            throw new IllegalStateException("Empréstimo já devolvido");
        }

        // Valida a data de devolução
        if (returnDate.isBefore(loan.getStartDate())) {
            log.error("Data de devolução {} não pode ser anterior à data de início {}", returnDate, loan.getStartDate());
            throw buildValidationException(
                    "Data de devolução não pode ser anterior à data de início",
                    "returnDate",
                    "Não pode ser anterior à data de início"
            );
        }

        // Atualiza o empréstimo
        loan.setEndDate(returnDate);

        try {
            loanIRepository.save(loan);
            log.info("Empréstimo devolvido com sucesso: {}", loanId);
        } catch (Exception e) {
            log.error("Falha ao devolver empréstimo: {}", e.getMessage());
            throw new RuntimeException("Erro ao devolver empréstimo", e);
        }
    }

    private LoanValidationException buildValidationException(String details, String fields, String fieldsMessage) {
        ValidationExceptionDetails exceptionDetails = ValidationExceptionDetails.builder()
                .timestamp(LocalDateTime.now())
                .status(400)
                .title("Erro de Validação")
                .details(details)
                .developerMessage(LoanValidationException.class.getName())
                .fields(fields)
                .fieldsMessage(fieldsMessage)
                .build();
        return new LoanValidationException(exceptionDetails);
    }

    @Override
    public Loan findById(Long id) {
        log.info("Buscando empréstimo com ID: {}", id);
        if (id == null || id <= 0) {
            log.error("ID do empréstimo inválido: {}", id);
            throw buildValidationException(
                    "ID do empréstimo deve ser positivo",
                    "id",
                    "Deve ser positivo"
            );
        }
        return loanIRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Empréstimo não encontrado para ID: {}", id);
                    return new EntityNotFoundException("Empréstimo não encontrado");
                });
    }

    @Override
    public List<Loan> findAll() {
        log.info("Buscando todos os empréstimos");
        return loanIRepository.findAll();
    }

}