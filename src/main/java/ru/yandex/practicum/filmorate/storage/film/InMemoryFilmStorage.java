package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.utils.MakerId;

import java.util.*;

@Repository
public class InMemoryFilmStorage implements FilmStorage {

    private final Map<Integer, Film> films = new HashMap<>();

    private final MakerId id = new MakerId();

    @Override
    public Film saveFilm(Film film) {
        if (film.getId() == null) film.setId(id.gen());
        films.put(film.getId(), film);
        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        if (film.getId() == null) film.setId(id.gen());
        films.put(film.getId(), film);
        return film;
    }

    @Override
    public Optional<Film> getFilm(Integer filmId) {
        return Optional.ofNullable(films.get(filmId));
    }

    @Override
    public List<Film> getAllFilms() {
        return new ArrayList<>(films.values());
    }

    @Override
    public Optional<Film> deleteFilm(Integer filmId) {
        films.remove(filmId);
        return Optional.empty();
    }

    @Override
    public Optional<Film> addLike(int filmId, int userId) {
        return Optional.empty();
    }

    @Override
    public Film removeLike(int filmId, int userId) {
        //TODO
        return null;
    }

    @Override
    public List<Film> getBestFilms(int count) {
        //TODO
        return null;
    }


    public void deleteAllFilms() {
        films.clear();
    }


    public Optional<List<Integer>> getLikeOfFilm(Integer filmId) {
        if (getFilm(filmId).isPresent()) {
            return Optional.of(new ArrayList<>(films.get(filmId).getLikes()));
        } else {
            return Optional.empty();
        }
    }
}
