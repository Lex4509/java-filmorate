# java-filmorate
<img width="682" alt="Снимок экрана 2022-12-07 в 19 42 37" src="https://user-images.githubusercontent.com/106984793/206292040-f7236686-57be-4e51-b95f-33250f4cf0fd.png">

---

## Примеры запросов:
### Найти пользователя
~~~
SELECT *
FROM users
WHERE user_id = ?;
~~~
### Найти всех пользователей
~~~
SELECT *
FROM users;
~~~
### Создать пользователя
~~~
INSERT INTO users (email, login, name, birthday)
VALUES (?, ?, ?, ?);
~~~
### Обновить пользователя
~~~
UPDATE users
SET email = ?,
    login = ?,
    name = ?,
    birthday = ?
WHERE user_id = ?;
~~~
### Удалить пользователя
~~~
DELETE FROM users
WHERE user_id = ?;
~~~
