package br.com.ifba.library.system.book.service;

import br.com.ifba.library.system.book.entity.Book;
import br.com.ifba.library.system.book.repository.BookIRepository;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class BookService implements BookIService{
    private final BookIRepository bookIRepository;


    @Override
    public Book saveBook(Book book) {
        return null;
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
