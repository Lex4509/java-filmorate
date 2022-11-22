delete from usr;
ALTER TABLE usr ALTER COLUMN id RESTART WITH 1;

insert into MPA_RATING (NAME, DESCRIPTION) VALUES ( 'G', 'rating G' );
-- select * from MPA_RATING;
insert into GENRE (NAME) values ( 'action' );
-- select * from FILM where ID = 3;
-- insert into FILM_GENRE (FILM_ID, GENRE_ID) VALUES ( 3, 1 );
-- select * from GENRE;
insert into film (NAME, DESCRIPTION, RELEASE_DATE, DURATION, MPA_RATING_ID) values ( 'Terminator2', 'terminator2 description', '2003-01-01', 90, 1 );
-- select * from FILM_GENRE;
-- select * from film left join FILM_GENRE FG on FILM.ID = FG.FILM_ID
-- delete from FILM where ID>3