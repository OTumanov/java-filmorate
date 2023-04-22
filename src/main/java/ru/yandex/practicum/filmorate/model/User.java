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

    //    @Email
    @NotBlank(message = "Отсутствует email")
    @Email(message = "Некорректный email")
    private String email;

    @NotNull
    @NotBlank
    private String login;

    private String name;
    @Past
    private LocalDate birthday;

    public User() {

    }


//    public User(String email, String login, String name, LocalDate birthday) {
//        this.email = email;
//        this.login = login;
//        this.name = (name == null || name.isEmpty() || name.isBlank()) ? login : name;
//        this.birthday = birthday;
//    }
//    public User(Integer id, String email, String login, String name, LocalDate birthday) {
//        this.id = id;
//        this.email = email;
//        this.login = login;
//        this.name = (name == null || name.isEmpty() || name.isBlank()) ? login : name;
//        this.birthday = birthday;
//    }

//    public void addAFriend(Integer friendId) {
//        friends.add(friendId);
//    }
//
//    public void removeAFriend(Integer friendId) {
//        friends.remove(friendId);
//    }
}
