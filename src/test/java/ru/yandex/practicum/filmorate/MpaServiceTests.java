package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.service.mpa.MpaService;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;


@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Sql({"/schema.sql", "/data.sql"})
public class MpaServiceTests {
    private final MpaService mpaService;

    @Test
    public void getAllMpaTest() {
        assertEquals(Arrays.asList(
                        new Mpa(1, "G"),
                        new Mpa(2, "PG"),
                        new Mpa(3, "PG-13"),
                        new Mpa(4, "R"),
                        new Mpa(5, "NC-17")),
                mpaService.getAllMpa());
    }

    @Test
    public void getMpaByIdTest() {
        List<Mpa> list = Arrays.asList(
                new Mpa(1, "G"),
                new Mpa(2, "PG"),
                new Mpa(3, "PG-13"),
                new Mpa(4, "R"),
                new Mpa(5, "NC-17"));

        for (Mpa mpa : list) {
            assertEquals(mpa, mpaService.getMpa(mpa.getId()));
        }
    }

    @Test
    public void mpaWithId9999NotFound() {
        Assertions.assertThatThrownBy(() ->
                mpaService.getMpa(9999)).isInstanceOf(ObjectNotFoundException.class);
    }
}