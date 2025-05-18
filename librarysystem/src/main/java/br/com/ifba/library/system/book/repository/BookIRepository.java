package br.com.ifba.library.system.book.repository;


import br.com.ifba.library.system.book.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookIRepository extends JpaRepository<Book, Long> {
}
