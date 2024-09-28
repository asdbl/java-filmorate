package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.Instant;

@Data
public class User {
    private Long id;
    @Email
    private String email;
    @NotNull
    @NotBlank
    private String login;
    private String name;
    private Instant birthday;
}