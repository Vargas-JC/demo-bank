package com.app.openx.demobank.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class User {
    private Integer id;
    private String name;
    private String dni;
    private LocalDateTime createdAt;
}
