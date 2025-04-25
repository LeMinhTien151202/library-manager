package com.project.library_backend.repositories;

import com.project.library_backend.models.BorrowStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BorrowStatusRepository extends JpaRepository<BorrowStatus, Long> {
}
