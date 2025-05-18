package br.com.ifba.library.system.book.service;

import br.com.ifba.library.system.book.entity.Book;
import br.com.ifba.library.system.book.repository.BookIRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
@Slf4j
@Service
@RequiredArgsConstructor
public class BookService implements BookIService{
    private final BookIRepository bookIRepository;


    @Override
    public Book saveBook(Book book) {

        try {
            log.info("Book Saved!");
            return bookIRepository.save(book);
        } catch (Exception e) {
            log.error("Book not saved: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public Book findById(Long id) {
        return null;
    }

    @Override
    public List<Book> findAll() {
        return List.of();
    }

    @Override
    public void deleteBook(Long id) {

    }

    @Override
    public Book updateBook(Book book) {
        return null;
    }
}
