INSERT INTO tasklist.users (name, username, password)
VALUES ('John Doe', 'johndoe@gmail.com', '$2a$10$HhQxQJQ0p1dGNjE26m7/h.pz4QKo8A4XQqMsGWYPhGbXm.0LWL4Q2'),
       ('Mile Smith', 'mikesmith@yahoo.com', '$2a$10$znviaRosD3LseEj.b7owHefiESUfceEGpYkynGupTOJmHyP7mM2ha');

INSERT INTO tasklist.tasks (title, description, status, expiration_date)
VALUES ('Bye cheese', NULL, 'TODO', '2023-06-05 12:00:00'),
       ('Do homework', 'Math, Physics, Literature', 'IN_PROGRESS', '2023-03-05 00:00:00'),
       ('Clean rooms', NULL, 'DONE', NULL),
       ('Call Mike', 'Ask about meeting', 'TODO', '2023-04-05 00:00:00');

INSERT INTO tasklist.users_tasks (task_id, user_id)
VALUES (1, 2),
       (2, 2),
       (3, 2),
       (4, 1);

INSERT INTO tasklist.users_roles (user_id, role)
VALUES (1, 'ROLE_ADMIN'),
       (1, 'ROLE_USER'),
       (2, 'ROLE_USER');
