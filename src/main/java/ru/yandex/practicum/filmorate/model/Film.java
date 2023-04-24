package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import ru.yandex.practicum.filmorate.utils.ReleaseDateValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
public class Film {
    Integer id;
    @NotBlank()
    String name;
    @Size(max = 200)
    String description;
    @FilmReleaseDateCheck()
    LocalDate releaseDate;
    @Positive()
    Long duration;
    private Mpa mpa;

    private List<Genre> genres;
    Set<Integer> likes = new HashSet<>();

    public Film(Integer id, String name, String description, LocalDate releaseDate, Long duration, Mpa mpa, List<Genre> genres) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
        this.mpa = mpa;
        this.genres = genres;
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Constraint(validatedBy = ReleaseDateValidator.class)
    public @interface FilmReleaseDateCheck {
        String message() default "Дата должна быть позже 28.12.1895!";

        Class<?>[] groups() default {};

        Class<? extends Payload>[] payload() default {};
    }
}
