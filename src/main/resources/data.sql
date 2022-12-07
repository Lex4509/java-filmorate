delete from friendship;
delete from film_like;
delete from users;
delete from FILM_GENRE;
delete from FILM_DIRECTOR;
delete from GENRE;
delete from DIRECTOR;
delete from FILM;
delete from MPA;
delete from events;

--удаление reviews
--начало
delete from reviews;
delete from reviews_likes;
--конец

ALTER TABLE users ALTER COLUMN user_id RESTART WITH 1;
ALTER TABLE film ALTER COLUMN film_id RESTART WITH 1;
ALTER TABLE GENRE ALTER COLUMN genre_id RESTART WITH 1;
ALTER TABLE DIRECTOR ALTER COLUMN director_id RESTART WITH 1;
ALTER TABLE MPA ALTER COLUMN mpa_id RESTART WITH 1;
ALTER TABLE events ALTER COLUMN event_id RESTART WITH 1;

--перезапуск нумератора id для reviews
--начало
ALTER TABLE reviews ALTER COLUMN review_id RESTART WITH 1;
--конец

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