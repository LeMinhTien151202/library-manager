package com.project.library_backend.dtos;

import lombok.*;

import java.time.LocalDateTime;

@Data//toString
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDTO {

    private String name;

    private String email;

    private String phone;

    private LocalDateTime createdAt;
}
