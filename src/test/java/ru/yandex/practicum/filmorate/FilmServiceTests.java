package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.film.FilmService;
import ru.yandex.practicum.filmorate.service.user.UserService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Sql({"/schema.sql", "/data.sql"})
class FilmServiceTests {
    private final FilmService filmService;
    private final UserService userService;
    private final Film film = new Film(
            1,
            "Film1",
            "descriptionFilm1",
            LocalDate.of(1999, 11, 5),
            120L, new Mpa(1, "G"),
            List.of(new Genre(1, "Комедия")));

    Film film2 = new Film(
            2,
            "Film2",
            "descriptionFIlm2",
            LocalDate.of(1980, 6, 11),
            118L,
            new Mpa(1, "G"),
            new ArrayList<>());

    private final User user = new User(
            1,
            "test@mail.ru",
            "LoginUser1",
            "NameUser1",
            LocalDate.of(1984, 5, 11));

    @Test
    public void addFilmAndGetFilmTest() {
        filmService.addFilm(film);

        assertEquals(film, filmService.getFilm(1));
    }

    @Test
    public void findAllFilmsTest() {
        filmService.addFilm(film);

        assertEquals(List.of(film), filmService.getAllFilms());
    }

    @Test
    public void updateMpaFromFilmTest() {
        filmService.addFilm(film);
        film.setMpa(new Mpa(2, "PG"));
        filmService.updateFilm(film);

        assertEquals((film), filmService.getFilm(1));
    }

    @Test
    public void updateFilmWithId9999NotFoundTest() {
        film.setId(9999);

        Assertions.assertThatThrownBy(() -> filmService.updateFilm(film)).isInstanceOf(ObjectNotFoundException.class);
    }

    @Test
    public void addAndDeleteLikeTest() {
        filmService.addFilm(film);
        filmService.addFilm(film2);
        userService.addUser(user);
        filmService.likeFilm(1, 1);

        assertEquals(List.of(film, film2), filmService.getTopFilms(3));

        filmService.removeLikeFilm(1, 1);
        filmService.likeFilm(2, 1);

        assertEquals(List.of(film2, film), filmService.getTopFilms(3));

    }
}
