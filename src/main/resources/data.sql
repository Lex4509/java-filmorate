delete from friendship;
delete from friendship_status;
delete from LIKES;
delete from usr;
delete from FILM_GENRE;
delete from GENRE;
delete from FILM;
delete from MPA_RATING;

ALTER TABLE usr ALTER COLUMN id RESTART WITH 1;
ALTER TABLE friendship_status ALTER COLUMN id RESTART WITH 1;
ALTER TABLE film ALTER COLUMN id RESTART WITH 1;
ALTER TABLE GENRE ALTER COLUMN id RESTART WITH 1;
ALTER TABLE MPA_RATING ALTER COLUMN id RESTART WITH 1;

INSERT INTO friendship_status (name) VALUES ('Подтвержденная');
INSERT INTO MPA_RATING (NAME, DESCRIPTION) VALUES ( 'G', 'у фильма нет возрастных ограничений' );
INSERT INTO MPA_RATING (NAME, DESCRIPTION) VALUES ( 'PG', 'детям рекомендуется смотреть фильм с родителями' );
INSERT INTO MPA_RATING (NAME, DESCRIPTION) VALUES ( 'PG-13', 'детям до 13 лет просмотр не желателен' );
INSERT INTO MPA_RATING (NAME, DESCRIPTION) VALUES ( 'R', 'лицам до 17 лет просматривать фильм можно только в присутствии взрослого' );
INSERT INTO MPA_RATING (NAME, DESCRIPTION) VALUES ( 'NC-17', 'лицам до 18 лет просмотр запрещён' );
INSERT INTO GENRE (NAME) VALUES ( 'Комедия' );
INSERT INTO GENRE (NAME) VALUES ( 'Драма' );
INSERT INTO GENRE (NAME) VALUES ( 'Мультфильм' );
INSERT INTO GENRE (NAME) VALUES ( 'Триллер' );
INSERT INTO GENRE (NAME) VALUES ( 'Документальный' );
INSERT INTO GENRE (NAME) VALUES ( 'Боевик' );

--insert into LIKES (USER_ID, FILM_ID) values ( 1,1 );

-- SELECT id, name, description, release_date, duration, mpa_rating_id FROM film f LEFT JOIN likes l ON f.id = l.film_id
--                 GROUP BY f.id
--                 ORDER BY COUNT(l.film_id) DESC
--                 LIMIT 8;
