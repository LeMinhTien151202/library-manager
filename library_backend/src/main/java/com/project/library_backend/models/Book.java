package com.project.library_backend.models;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "books")
@Data//toString
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "book_id")
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String author;

    @Column(name = "publication_year")
    private Long publicationYear;

    private String genre;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    private String thumbnail;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}
