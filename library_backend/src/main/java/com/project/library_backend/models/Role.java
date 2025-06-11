package com.project.library_backend.models;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "roles")
@Data//toString
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "role_name", nullable = false)
    private String name;
}
