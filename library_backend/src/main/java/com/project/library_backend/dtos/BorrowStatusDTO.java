package com.project.library_backend.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BorrowStatusDTO {
    @JsonProperty("book_id")
    private Long bookId;

    @JsonProperty("borrower_id")
    private Long borrowerId;

    @JsonProperty("borrow_date")
    private LocalDate borrowDate;

    @JsonProperty("return_date")
    private LocalDate returnDate;

    @JsonProperty("status")
    private String status;

}
