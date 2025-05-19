package br.com.ifba.library.system.book.controller;

import br.com.ifba.library.system.book.dto.BookDto;
import br.com.ifba.library.system.book.entity.Book;
import br.com.ifba.library.system.book.service.BookIService;
import br.com.ifba.library.system.mapper.ObjectMapperUtil;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/books")
@RequiredArgsConstructor
public class BookController {

    private final BookIService bookService;
    private final ObjectMapperUtil objectMapperUtil;

    @PostMapping
    public ResponseEntity<BookDto> createBook(@Valid @RequestBody BookDto bookDto) {
        Book book = objectMapperUtil.map(bookDto, Book.class);
        Book savedBook = bookService.saveBook(book);
        BookDto savedBookDTO = objectMapperUtil.map(savedBook, BookDto.class);
        return new ResponseEntity<>(savedBookDTO, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookDto> getBookById(@PathVariable Long id) {
        Book book = bookService.findById(id);
        BookDto bookDto = objectMapperUtil.map(book, BookDto.class);
        return ResponseEntity.ok(bookDto);
    }

    @GetMapping
    public ResponseEntity<List<BookDto>> getAllBooks() {
        List<Book> books = bookService.findAll();
        List<BookDto> bookDTOs = objectMapperUtil.mapAll(books, BookDto.class);
        return ResponseEntity.ok(bookDTOs);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BookDto> updateBook(@PathVariable Long id, @Valid @RequestBody BookDto bookDTO) {
        if (!id.equals(bookDTO.getId())) {
            throw new IllegalArgumentException("ID in path must match ID in body");
        }
        Book book = objectMapperUtil.map(bookDTO, Book.class);
        Book updatedBook = bookService.updateBook(book);
        BookDto updatedBookDTO = objectMapperUtil.map(updatedBook, BookDto.class);
        return ResponseEntity.ok(updatedBookDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable Long id) {
        bookService.deleteBook(id);
        return ResponseEntity.noContent().build();
    }
}