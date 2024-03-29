CREATE TABLE if not exists mpa (
    rating_id int GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    rating_name varchar(40) NOT NULL
);

CREATE TABLE if not exists genres (
    genre_id int GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    genre_name varchar(40)
);

CREATE TABLE if not exists films (
    film_id int GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    film_name varchar(40),
    film_description varchar(200),
    releaseDate date,
    duration int,
    rating_id int REFERENCES MPA (rating_id),
    genre_id int REFERENCES GENRES (genre_id)
);

CREATE TABLE if not exists users (
    user_id int GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    user_email varchar(40),
    user_login varchar(40),
    user_name varchar(40),
    birthday date
);


CREATE TABLE if not exists films_genres (
    genre_id int REFERENCES genres (genre_id),
    film_id int REFERENCES films (film_id),
    CONSTRAINT pk_films_genres PRIMARY KEY (
                                      genre_id, film_id
        )
    );

CREATE TABLE if not exists likes (
    film_id int REFERENCES films (film_id),
    user_id int REFERENCES users (user_id),
    CONSTRAINT pk_likes PRIMARY KEY (
                                          user_id, film_id
        )
);

CREATE TABLE if not exists friends (
    user_id int REFERENCES users (user_id),
    friend_id int REFERENCES users (user_id),
    CONSTRAINT pk_friends PRIMARY KEY (
                                     user_id, friend_id
        )
);