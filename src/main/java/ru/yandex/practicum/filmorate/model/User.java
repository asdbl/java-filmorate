package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class User {
    private Long id;
    @NotEmpty(message = "Имейл не может быть пустым")
    @Email
    private String email;
    @NotBlank(message = "Логин не может быть пустым")
    @Pattern(regexp = "^[a-zA-Z0-9]{1,12}$", message = "Логин не должен содержать пробел и специальные символы")
    private String login;
    private String name;
    @NotNull
    @PastOrPresent(message = "День рождения не может быть в будущем")
    private LocalDate birthday;
}
