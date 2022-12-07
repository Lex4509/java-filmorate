delete from friendship;
delete from film_like;
delete from users;
delete from FILM_GENRE;
delete from GENRE;
delete from FILM;
delete from MPA;

<<<<<<< HEAD
=======
--удаление reviews
--начало
delete from reviews;
delete from reviews_likes;
--конец

>>>>>>> origin/develop
ALTER TABLE users ALTER COLUMN user_id RESTART WITH 1;
ALTER TABLE film ALTER COLUMN film_id RESTART WITH 1;
ALTER TABLE GENRE ALTER COLUMN genre_id RESTART WITH 1;
ALTER TABLE MPA ALTER COLUMN mpa_id RESTART WITH 1;

<<<<<<< HEAD
=======
--перезапуск нумератора id для reviews
--начало
ALTER TABLE reviews ALTER COLUMN review_id RESTART WITH 1;
--конец

>>>>>>> origin/develop
MERGE INTO genre KEY (genre_id) VALUES (1, 'Комедия');
MERGE INTO genre KEY (genre_id) VALUES (2, 'Драма');
MERGE INTO genre KEY (genre_id) VALUES (3, 'Мультфильм');
MERGE INTO genre KEY (genre_id) VALUES (4, 'Триллер');
MERGE INTO genre KEY (genre_id) VALUES (5, 'Документальный');
MERGE INTO genre KEY (genre_id) VALUES (6, 'Боевик');

MERGE INTO mpa KEY (mpa_id) VALUES (1, 'G');
MERGE INTO mpa KEY (mpa_id) VALUES (2, 'PG');
MERGE INTO mpa KEY (mpa_id) VALUES (3, 'PG-13');
MERGE INTO mpa KEY (mpa_id) VALUES (4, 'R');
MERGE INTO mpa KEY (mpa_id) VALUES (5, 'NC-17');