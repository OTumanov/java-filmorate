package ru.yandex.practicum.filmorate.service.mpa;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.mpa.MpaStorage;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class MpaService implements MpaServiceInterface {
    private final MpaStorage mpaStorage;

    @Override
    public List<Mpa> findAll() {
        log.info("Сервис: Получение всех MPA");
        return mpaStorage.getAll();
    }

    @Override
    public Mpa getById(int id) {
        log.info("Сервис: Получение MPA с id");
        return mpaStorage.getById(id);
    }
}
