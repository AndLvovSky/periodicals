INSERT INTO publication (id, cost, description, name, period) VALUES
(1, 5, '-',	'Tampa Bay Times', 7),
(2,	7.5, '-', 'Minneapolis Star Tribune', 30),
(3, 10,	'-', 'Philadelphia Inquirer',  7),
(4,	3.75, '-', 'St. Louis Post-Dispatch', 7),
(5,	11,	'-', 'New Jersey Star-Ledger', 14),
(6,	16.2, '-', 'Arizona Republic', 21);

INSERT INTO privilege (id, name) VALUES
(1,	'READ_PUBLICATIONS'),
(2,	'EDIT_PUBLICATIONS');

INSERT INTO role (id, name) VALUES
(1,	'ROLE_ADMIN'),
(2,	'ROLE_USER');

INSERT INTO role_privileges (roles_id, privileges_id) VALUES
(1,	2),
(2,	1);

INSERT INTO user_entity (id, name, password) VALUES
(1,	'a', 'p'),
(2,	'u', 'p');

INSERT INTO user_entity_roles (users_id, roles_id) VALUES
(1,	1),
(1,	2),
(2,	2);

CREATE SEQUENCE publication_sequence
    START WITH 7
    INCREMENT BY 1;
