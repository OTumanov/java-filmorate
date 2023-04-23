package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import java.time.LocalDate;

@Data
@AllArgsConstructor
public class User {
    private Integer id;

    @NotBlank(message = "Пустое поле email")
    @Email(message = "Плохой формат поля email")
    private String email;

    @NotNull
    @NotBlank
    private String login;

    private String name;
    @Past
    private LocalDate birthday;

    public User() {}
}
