package com.project.library_backend.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.project.library_backend.models.Borrower;
import com.project.library_backend.services.BorrowerService;
import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDate;

@Slf4j
@SpringBootTest
@AutoConfigureMockMvc
public class BorrowerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private BorrowerService borrowerService;

    private Borrower borrower;

    private Borrower borrowerData;

    @BeforeEach
    void initData(){
        borrower = Borrower.builder()
                .name("Tien le")
                .email("tienle1@gmail.com")
                .phone("0987654321").build();

        borrowerData = Borrower.builder()
                .name("Tien le")
                .email("tienle1@gmail.com")
                .phone("0987654321091").build();
    }

    @Test
    void createBorrower_success() throws Exception {
        // GIVEN
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        String content = objectMapper.writeValueAsString(borrower);

        Mockito.when(borrowerService.createBorrower(ArgumentMatchers.any()))
                .thenReturn(borrower);

        // WHEN, THEN
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/v1/borrowers")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(content))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("name")
                        .value("Tien le"))
                .andExpect(MockMvcResultMatchers.jsonPath("phone")
                        .value("0987654321")

                );
    }

    @Test
    void createBorrower_phone_fail() throws Exception {
        // GIVEN
        // Tạo một Borrower với số điện thoại KHÔNG HỢP LỆ (ví dụ: quá dài)
        // Sử dụng một instance Borrower mới hoặc tạo dữ liệu JSON trực tiếp để tránh ảnh hưởng đến sampleBorrower dùng trong các test khác
        Borrower borrowerWithInvalidPhone = new Borrower();
        borrowerWithInvalidPhone.setName("Valid Name"); // Giả sử tên hợp lệ
        borrowerWithInvalidPhone.setEmail("valid.email@example.com"); // Giả sử email hợp lệ
        borrowerWithInvalidPhone.setPhone("098765432108"); // <-- Số điện thoại quá dài, gây lỗi validation

        ObjectMapper objectMapper = new ObjectMapper();
        // Đăng ký JavaTimeModule nếu Borrower có trường LocalDateTime và bạn serialize nó
        objectMapper.registerModule(new JavaTimeModule());
        String content = objectMapper.writeValueAsString(borrowerWithInvalidPhone);

        // KHÔNG CẦN MOCK borrowerService.createBorrower() ở đây
        // vì test này kiểm tra luồng lỗi validation ở tầng Controller, không phải logic của Service

        // WHEN, THEN
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/v1/borrowers")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(content))
                // 1. Assertion chính: Kiểm tra status code
                // is4xxClientError() hoặc cụ thể hơn là isBadRequest() (400)
                .andExpect(MockMvcResultMatchers.status().isBadRequest());

        // 2. (Tùy chọn) Nếu server trả về một chuỗi lỗi cụ thể, kiểm tra nội dung chuỗi đó
        // Ví dụ: Nếu server trả về body là "Invalid phone number format" cho lỗi 400
        // .andExpect(MockMvcResultMatchers.content().string("Thông báo lỗi mong đợi từ server"));

        // XÓA BỎ assertion jsonPath vì response body không phải là JSON object của Borrower
        // .andExpect(MockMvcResultMatchers.jsonPath("phone").value("0987654321")); // <-- Xóa dòng này
    }

    @Test
        //
    void createUser_usernameInvalid_fail() throws Exception {
        // GIVEN
        //request.setUsername("joh");

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        String content = objectMapper.writeValueAsString(borrowerData);

        // WHEN, THEN
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/v1/borrowers")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(content))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("message")
                        .value("Phone must be between 10 and 12 characters")
                );
    }

}
