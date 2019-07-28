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

CREATE SEQUENCE hibernate_sequence
    START WITH 101
    INCREMENT BY 1;
