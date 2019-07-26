CREATE TABLE privilege (
    id bigint NOT NULL PRIMARY KEY,
    name character varying(255)
);

CREATE TABLE publication (
    id bigint NOT NULL PRIMARY KEY,
    cost double precision,
    description character varying(255),
    name character varying(255),
    period integer
);

CREATE TABLE role (
    id bigint NOT NULL PRIMARY KEY,
    name character varying(255)
);

CREATE TABLE role_privileges (
    roles_id bigint NOT NULL,
    privileges_id bigint NOT NULL,
    FOREIGN KEY (roles_id) REFERENCES role(id),
    FOREIGN KEY (privileges_id) REFERENCES privilege(id)
);

CREATE TABLE user_entity (
    id bigint NOT NULL PRIMARY KEY,
    name character varying(255),
    password character varying(255)
);

CREATE TABLE user_entity_roles (
    users_id bigint NOT NULL,
    roles_id bigint NOT NULL,
    FOREIGN KEY (roles_id) REFERENCES role(id),
    FOREIGN KEY (users_id) REFERENCES user_entity(id)
);

INSERT INTO privilege (id, name) VALUES
(1,	'READ_PUBLICATIONS'),
(2,	'EDIT_PUBLICATIONS');

INSERT INTO publication (id, cost, description, name, period) VALUES
(1, 5, '-',	'Tampa Bay Times', 7),
(2,	7.5, '-', 'Minneapolis Star Tribune', 30),
(3, 10,	'-', 'Philadelphia Inquirer',  7),
(4,	3.75, '-', 'St. Louis Post-Dispatch', 7),
(5,	11,	'-', 'New Jersey Star-Ledger', 14),
(6,	16.2, '-', 'Arizona Republic', 21);

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
