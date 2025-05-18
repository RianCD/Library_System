package br.com.ifba.library.system.book.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Date;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
public class Book {
    @Id
    private Long id;

    private String title;
    private String author;
    private String isbn;
    private Date publishedDate;

}