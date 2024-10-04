package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import javax.validation.constraints.*;
import java.time.LocalDate;

@Data
public class User {
    private Long id;
    @NotEmpty
    @Email
    private String email;
    @NotBlank
    @Pattern(regexp = "^[a-zA-Z0-9]{1,12}$", message = "Логин не должен содержать пробел и специальные символы")
    private String login;
    private String name;
    @NotNull
    @PastOrPresent
    private LocalDate birthday;
}
