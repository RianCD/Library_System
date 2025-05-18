package br.com.ifba.library.system.book.service;

import br.com.ifba.library.system.book.entity.Book;
import br.com.ifba.library.system.book.repository.BookIRepository;
import br.com.ifba.library.system.loan.repository.LoanIRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class BookService implements BookIService {
    private final BookIRepository bookIRepository;
     private final LoanIRepository loanIRepository;

    @Override
    public Book saveBook(Book book) {
        log.info("Attempting to save book: {}", book);

        // Validação
        validateBook(book);
        if (book.getId() != null) {
            log.error("Book ID must be null for new books");
            throw new IllegalArgumentException("Cannot save book with predefined ID");
        }

        try {
            Book savedBook = bookIRepository.save(book);
            log.info("Book saved successfully: {}", savedBook);
            return savedBook;
        } catch (Exception e) {
            log.error("Failed to save book: {}", e.getMessage());
            throw new RuntimeException("Error saving book", e);
        }
    }

    @Override
    public Book findById(Long id) {
        log.info("Finding book by ID: {}", id);

        // Validação do ID
        if (id == null || id <= 0) {
            log.error("Invalid ID: {}", id);
            throw new IllegalArgumentException("ID must be positive");
        }

        Optional<Book> book = bookIRepository.findById(id);
        if (book.isEmpty()) {
            log.error("Book not found for ID: {}", id);
            throw new EntityNotFoundException("Book not found");
        }

        log.info("Book found: {}", book.get());
        return book.get();
    }

    @Override
    public List<Book> findAll() {
        log.info("Listing all books");
        List<Book> books = bookIRepository.findAll();
        log.info("Found {} books", books.size());
        return books;
    }

    @Override
    public void deleteBook(Long id) {
        log.info("Attempting to delete book with ID: {}", id);

        // Validação do ID
        if (id == null || id <= 0) {
            log.error("Invalid ID: {}", id);
            throw new IllegalArgumentException("ID must be positive");
        }

        // Verifica se o livro existe
        Book book = bookIRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Book not found for ID: {}", id);
                    return new EntityNotFoundException("Book not found");
                });

        try {
            bookIRepository.deleteById(id);
            log.info("Book deleted successfully: {}", id);
        } catch (Exception e) {
            log.error("Failed to delete book: {}", e.getMessage());
            throw new RuntimeException("Error deleting book", e);
        }
    }

    @Override
    public Book updateBook(Book book) {
        log.info("Attempting to update book: {}", book);

        // Validações
        validateBook(book);
        if (book.getId() == null) {
            log.error("Book ID is required for update");
            throw new IllegalArgumentException("Book ID is required");
        }

        // Verifica se o livro existe
        Book existingBook = bookIRepository.findById(book.getId())
                .orElseThrow(() -> {
                    log.error("Book not found for ID: {}", book.getId());
                    return new EntityNotFoundException("Book not found");
                });

        // Atualiza os campos
        existingBook.setTitle(book.getTitle());
        existingBook.setAuthor(book.getAuthor());
        existingBook.setPublishedDate(book.getPublishedDate());

        try {
            Book updatedBook = bookIRepository.save(existingBook);
            log.info("Book updated successfully: {}", updatedBook);
            return updatedBook;
        } catch (Exception e) {
            log.error("Failed to update book: {}", e.getMessage());
            throw new RuntimeException("Error updating book", e);
        }
    }

    private void validateBook(Book book) {
        if (book == null) {
            throw new IllegalArgumentException("Book cannot be null");
        }
        if (book.getTitle() == null || book.getTitle().trim().isEmpty()) {
            throw new IllegalArgumentException("Title is required");
        }
        if (book.getAuthor() == null || book.getAuthor().trim().isEmpty()) {
            throw new IllegalArgumentException("Author is required");
        }
        if (book.getPublishedDate() != null) {
            LocalDate publishedDate = book.getPublishedDate().toInstant()
                    .atZone(java.time.ZoneId.systemDefault()).toLocalDate();
            if (publishedDate.isAfter(LocalDate.now())) {
                throw new IllegalArgumentException("Published date cannot be in the future");
            }
        }
    }
}