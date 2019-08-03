INSERT INTO publication (id, name, period, cost, description) VALUES
(1, 'Tampa Bay Times', 7, 5, 'Customers who purchase a Tampa Bay Times subscription receive comprehensive and in-depth news coverage. A well respected newspaper with strong local focus, compelling feature articles and sharp editorial content, Tampa Bay Times newspaper readers are always well informed. Order Tampa Bay Times home delivery on the days that are the most convenient for you. Tampa Bay Times subscription offers are listed below. Don’t miss another issue, subscribe today!'),
(2,	'Minneapolis Star Tribune', 30, 7.5, 'Customers who purchase a Minneapolis Star Tribune subscription receive comprehensive and in-depth news coverage. A well respected newspaper with strong local focus, compelling feature articles and sharp editorial content, Minneapolis Star Tribune newspaper readers are always well informed. Order Minneapolis Star Tribune home delivery on the days that are the most convenient for you. Minneapolis Star Tribune subscription offers are listed below. Don’t miss another issue, subscribe today!'),
(3, 'Philadelphia Inquirer', 7, 10,	'Customers who purchase a Philadelphia Inquirer subscription receive comprehensive and in-depth news coverage. A well respected newspaper with strong local focus, compelling feature articles and sharp editorial content, Philadelphia Inquirer newspaper readers are always well informed. Order Philadelphia Inquirer home delivery on the days that are the most convenient for you. Philadelphia Inquirer subscription offers are listed below. Don’t miss another issue, subscribe today!'),
(4,	'St. Louis Post-Dispatch', 7, 3.75, 'Customers who purchase a St. Louis Post-Dispatch subscription receive comprehensive and in-depth news coverage. A well respected newspaper with strong local focus, compelling feature articles and sharp editorial content, St. Louis Post-Dispatch newspaper readers are always well informed. Order St. Louis Post-Dispatch home delivery on the days that are the most convenient for you. St. Louis Post-Dispatch subscription offers are listed below. Don’t miss another issue, subscribe today!'),
(5,	'New Jersey Star-Ledger', 14, 11,	'Customers who purchase a New Jersey Star-Ledger subscription receive comprehensive and in-depth news coverage. A well respected newspaper with strong local focus, compelling feature articles and sharp editorial content, New Jersey Star-Ledger newspaper readers are always well informed. Order New Jersey Star-Ledger home delivery on the days that are the most convenient for you. New Jersey Star-Ledger subscription offers are listed below. Don’t miss another issue, subscribe today!'),
(6,	'Arizona Republic', 21, 16.2, 'Customers who purchase a Arizona Republic subscription receive comprehensive and in-depth news coverage. A well respected newspaper with strong local focus, compelling feature articles and sharp editorial content, Arizona Republic newspaper readers are always well informed. Order Arizona Republic home delivery on the days that are the most convenient for you. Arizona Republic subscription offers are listed below. Don’t miss another issue, subscribe today!');

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

CREATE EXTENSION pgcrypto;
UPDATE user_entity SET password = crypt(password, gen_salt('bf'));

INSERT INTO user_entity_roles (users_id, roles_id) VALUES
(1,	1),
(1,	2),
(2,	2);

CREATE SEQUENCE publication_sequence
    START WITH 7
    INCREMENT BY 1;
