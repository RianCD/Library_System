package br.com.ifba.library.system.book.service;

import br.com.ifba.library.system.book.entity.Book;

import java.util.List;

public interface BookIService {
    Book saveBook(Book book);
    Book findById(Long id);
    List<Book> findAll();
    void deleteBook(Long id);
    Book updateBook(Book book);
}
