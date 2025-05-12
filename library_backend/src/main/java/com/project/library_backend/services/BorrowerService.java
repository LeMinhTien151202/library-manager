package com.project.library_backend.services;

import com.project.library_backend.exceptions.DataNotFoundException;
import com.project.library_backend.models.Borrower;
import com.project.library_backend.repositories.BorrowerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class BorrowerService implements IBorrowerService{

    private final BorrowerRepository borrowerRepository;

    @Override
    public List<Borrower> getAllBorrowers() {
        return borrowerRepository.findAll();
    }

    @Override
    public Borrower getBorrowerById(Long id) throws Exception{
        return borrowerRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Cannot find borrower with id: "+id));
    }

    @Override
    public Borrower createBorrower(Borrower borrower) {

        log.info("service: create borrower");

        if (borrowerRepository.existsByPhone(borrower.getPhone())){
            throw new RuntimeException("phone existed.");
        }

        Borrower newBorrower = Borrower.builder()
                .phone(borrower.getPhone())
                .name(borrower.getName())
                .email(borrower.getEmail()).build();
        return borrowerRepository.save(newBorrower);
    }

    @Override
    public Borrower updateBorrower(Long id, Borrower borrower) throws Exception {
        Borrower borrower1 = borrowerRepository.findById(id)
                .orElseThrow(() ->
                        new DataNotFoundException(
                                "Cannot find borrower with id: "+id));
        borrower1.setName(borrower.getName());
        borrower1.setEmail(borrower.getEmail());
        borrower1.setPhone(borrower.getPhone());
        return borrowerRepository.save(borrower1);
    }

    @Override
    public void deleteBorrower(Long id) throws DataNotFoundException {
        if (!borrowerRepository.existsById(id)) {
            throw new DataNotFoundException("Cannot find borrower with id: " + id);
        }
        borrowerRepository.deleteById(id);
    }
}
