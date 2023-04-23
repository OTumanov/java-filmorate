package ru.yandex.practicum.filmorate.service.genre;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.genre.GenreStorage;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class GenreService implements GenreServiceInterface {
    private final GenreStorage genreStorage;

    @Override
    public List<Genre> findAll() {
        log.info("Сервис: Получение всех жанров");
        return genreStorage.findAll();
    }

    @Override
    public Genre getById(int id) {
        log.info("Сервис: Получение жанра с id");
        return genreStorage.getById(id);
    }
}
