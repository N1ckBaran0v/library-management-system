CREATE TABLE IF NOT EXISTS `books`
(
    book_id         INTEGER PRIMARY KEY AUTOINCREMENT,
    title           TEXT    NOT NULL,
    author          TEXT    NOT NULL,
    genre           TEXT    NOT NULL,
    total_count     INTEGER NOT NULL,
    available_count INTEGER NOT NULL
);
CREATE TABLE IF NOT EXISTS `users`
(
    username TEXT NOT NULL PRIMARY KEY,
    password TEXT NOT NULL,
    name     TEXT NOT NULL,
    phone    TEXT NOT NULL,
    role     TEXT NOT NULL
);
CREATE TABLE IF NOT EXISTS `history`
(
    id             INTEGER PRIMARY KEY AUTOINCREMENT,
    username       TEXT      NOT NULL,
    book_id        INTEGER   NOT NULL,
    operation_type TEXT      NOT NULL,
    operation_time TIMESTAMP NOT NULL
);