DELETE
FROM books;
DELETE
FROM history;
DELETE
FROM users;

INSERT INTO users (username, password, name, phone, role)
VALUES ('adminUser', 'admin', 'admin', '8(900) 000-00-00', 'admin');
INSERT INTO users (username, password, name, phone, role)
VALUES ('workerUser', 'worker', 'worker', '8(900) 000-00-01', 'worker');
INSERT INTO users (username, password, name, phone, role)
VALUES ('userUser', 'user', 'user', '8(900) 000-00-02', 'user');

INSERT INTO books (title, author, genre, total_count, available_count)
VALUES ('Война и мир', 'Толстой Л. Н.', 'Роман-эпопея', 4, 4);
INSERT INTO books (title, author, genre, total_count, available_count)
VALUES ('Мёртвые души', 'Гоголь Н. В.', 'Поэма', 1, 1);
INSERT INTO books (title, author, genre, total_count, available_count)
VALUES ('Шинель', 'Гоголь Н. В.', 'Повесть', 4, 4);
INSERT INTO books (title, author, genre, total_count, available_count)
VALUES ('Руслан и Людмила', 'Пушкин А. С.', 'Поэма', 4, 4);
