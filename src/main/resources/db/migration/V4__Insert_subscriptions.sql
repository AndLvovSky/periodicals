INSERT INTO subscription (id, owner_id, publication_id, number) VALUES
(1, 2, 4, 5),
(2, 2, 3, 10);

CREATE SEQUENCE subscription_sequence
    START WITH 3
    INCREMENT BY 1;
