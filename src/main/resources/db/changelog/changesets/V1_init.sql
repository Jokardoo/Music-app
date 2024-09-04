CREATE TABLE IF NOT EXISTS artists
(
    id          bigserial primary key,
    name        varchar(255) not null unique,
    description varchar(1023)
);

CREATE TABLE IF NOT EXISTS genres
(
    id   bigserial primary key,
    name varchar(255) not null unique
);

CREATE TABLE IF NOT EXISTS tracks
(
    id            bigserial primary key,
    name          varchar(255) not null,
    full_time     int          not null,
    download_link varchar(255),
    trackGenre    varchar(255),
    artist        varchar(255) not null,
    artist_id     bigint,
    file          varchar(255)
);

CREATE TABLE IF NOT EXISTS users
(
    id       bigserial primary key,
    name     varchar(255) not null,
    username varchar(255) not null,
    password varchar(255) not null,
    email    varchar(255) not null
);

CREATE TABLE IF NOT EXISTS artists_tracks
(
    artist_id bigint not null,
    track_id  bigint not null,
    primary key (artist_id, track_id),
    constraint fk_artists_tracks_artists foreign key (artist_id) references artists (id) on delete cascade on update no action,
    constraint fk_artists_tracks_tracks foreign key (track_id) references tracks (id) on delete cascade on update no action
);

CREATE TABLE IF NOT EXISTS artists_genres
(
    artist_id bigint       not null,
    genre     varchar(255) not null,
    primary key (artist_id, genre),
    constraint fk_artists_genres_artists foreign key (artist_id) references artists (id) on delete cascade on update no action
);

CREATE TABLE IF NOT EXISTS tracks_genres
(
    track_id bigint       not null,
    genre    varchar(255) not null,
    primary key (track_id, genre),
    constraint fk_artists_genres_artists foreign key (track_id) references tracks (id) on delete cascade on update no action
);



CREATE TABLE IF NOT EXISTS users_tracks
(
    user_id  bigint not null
        constraint fk_users_tracks_users
            references users
            on delete cascade,
    track_id bigint not null
        constraint fk_users_tracks_tracks
            references tracks
            on delete cascade,
    primary key (user_id, track_id)
);

CREATE TABLE IF NOT EXISTS users_roles
(
    user_id bigint       not null,
    role    varchar(255) not null,
    primary key (user_id, role),
    constraint fk_users_roles_users foreign key (user_id) references users (id) on delete cascade on update no action
);

CREATE TABLE IF NOT EXISTS tracks_files
(
    track_id bigint       not null,
    file     varchar(255) not null,
    constraint fk_tracks_files_tracks foreign key (track_id) references tracks (id) on delete cascade on update no action
);