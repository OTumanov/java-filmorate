package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.utils.FilmChecker;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class FilmsTest {
    static Film testFilm = new Film(
            "Терминатор-2",
            "И, восстали машины из пепла ядерной войны!..",
            LocalDate.of(1991, 12, 25),
            137);


    @AfterEach
    public void film() {
        testFilm = new Film(
                "Терминатор-2",
                "И, восстали машины из пепла ядерной войны!..",
                LocalDate.of(1991, 12, 25),
                137);
    }

    @Test
    public void filmCheckTest() {
        FilmChecker filmChecker = new FilmChecker(testFilm);
        assertTrue(filmChecker.check());
    }

    @Test
    public void checkNameTest() {
        FilmChecker filmChecker = new FilmChecker(testFilm);
        testFilm.setName(" ");
        assertFalse(filmChecker.check());
        testFilm.setName("");
        assertFalse(filmChecker.check());
    }

    @Test
    public void checkLengthDescriptionTest() {
        FilmChecker filmChecker = new FilmChecker(testFilm);
        testFilm.setDescription("съешь ещё этих мягких французских булок, да выпей чаю");
        assertTrue(filmChecker.check());
        testFilm.setDescription("В четверг четвертого числа в четыре с четвертью часа\n" +
                "лигурийский регулировщик регулировал в Лигурии,\n" +
                "но тридцать три корабля лавировали, лавировали, да так и не вылавировали,\n" +
                "а потом протокол про протокол протоколом запротоколировал,\n" +
                "как интервьюером интервьюируемый лигурийский регулировщик речисто,\n" +
                "да не чисто рапортовал, да не дорапортовал дорапортовывал\n" +
                "да так зарапортовался про размокропогодившуюся погоду\n" +
                "что, дабы инцидент не стал претендентом на судебный прецедент");
        assertFalse(filmChecker.check());
    }

    @Test
    public void checkReleaseDateTest() {
        FilmChecker filmChecker = new FilmChecker(testFilm);
        testFilm.setReleaseDate(LocalDate.of(1880, 1, 1));
        assertFalse(filmChecker.check());
        testFilm.setReleaseDate(LocalDate.now().plusDays(1));
        assertFalse(filmChecker.check());
    }

    @Test
    public void checkDurationTest() {
        FilmChecker filmChecker = new FilmChecker(testFilm);
        testFilm.setDuration(-1L);
        assertFalse(filmChecker.check());
        testFilm.setDuration(150L);
        assertTrue(filmChecker.check());
    }
}
