package com.project.library_backend.repositories;

import com.project.library_backend.models.Borrower;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BorrowerRepository extends JpaRepository<Borrower, Long> {
    boolean existsByPhone(String phone);
}
