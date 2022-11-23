package ru.yandex.practicum.filmorate.aplicationTest.user;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserDbStorage;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class UserDaoTest {
    private final UserDbStorage userStorage;

    @BeforeEach
    public void init(){

        String birthday = "1990-01-01";
        User user = new User(1,"email@email.ru", "login", "name", LocalDate.parse(birthday));
        userStorage.create(user);
    }

    @Test
    public void testFindUserById() {

        Optional<User> userOptional = Optional.ofNullable(userStorage.findById(1));

        assertThat(userOptional)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("id", 1)
                );
    }

    @Test
    public void testFindUsers() {

        Optional<User> userOptional = Optional.ofNullable(userStorage.findAll().get(0));

        assertThat(userOptional)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("id", 1)
                );
    }

    @Test
    public void updateUser() {

        Optional<User> userOptional = Optional.ofNullable(userStorage.update(
                new User(1,"email@email.ru", "login", "name upd", LocalDate.parse("1989-01-01"))));

        assertThat(userOptional)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("name", "name upd")
                );
    }
}
