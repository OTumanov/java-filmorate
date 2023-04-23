package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.film.FilmService;
import ru.yandex.practicum.filmorate.service.user.UserService;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

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
            "descriptionFilm11",
            LocalDate.of(1999, 11, 5),
            120L, new Mpa(1, "G"));

    private final User user = new User(1, "testEmail", "testLogin", "testName",
            LocalDate.of(1989, 3, 12));

    @Test
    public void addAndGetFilmTest() {
        filmService.addFilm(film);
        assertEquals(Optional.of(film), filmService.getFilm(1));
    }

    @Test
    public void findAllFilmsTest() {
        filmService.addFilm(film);
        assertEquals(List.of(film), filmService.getAllFilms());
    }
//
//    @Test
//    public void updateFilmTest() {
//        filmService.create(film);
//        film.setMpa(new Mpa(3, "PG-13"));
//        filmService.update(film);
//        assertEquals(film, filmService.getById(1));
//    }
//
//    @Test
//    public void deleteFilmTest() {
//        filmService.create(film);
//        filmService.deleteById(1);
//        assertEquals(new ArrayList<>(), filmService.findAll());
//    }
//
//    @Test
//    public void testUpdateFilmNotFound() {
//        film.setId(999);
//        Assertions.assertThatThrownBy(() -> filmService.update(film)).isInstanceOf(ObjectNotFoundException.class);
//    }
//
//    @Test
//    public void addAndDeleteLikeTest() {
//        filmService.create(film);
//        Film film2 = new Film(2, "name2", "description1",
//                LocalDate.of(2023, 1, 19), 100, null, new Mpa(1, "G"));
//        filmService.create(film2);
//        userService.create(user);
//        filmService.addLike(1, 1);
//        assertEquals(List.of(film, film2), filmService.getBestFilms(3));
//        filmService.removeLike(1, 1);
//        filmService.addLike(2, 1);
//        assertEquals(List.of(film2, film), filmService.getBestFilms(3));

//    }
}
