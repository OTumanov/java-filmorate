package ru.yandex.practicum.filmorate.service.genre;

import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;

public interface GenreServiceInterface {
    List<Genre> findAllGenres();

    Genre getGenre(int id);
}