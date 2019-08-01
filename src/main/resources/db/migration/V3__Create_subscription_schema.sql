CREATE TABLE subscription (
    id bigint NOT NULL PRIMARY KEY,
    owner_id bigint,
    publication_id bigint,
    number int,
    FOREIGN KEY (owner_id) REFERENCES user_entity(id),
    FOREIGN KEY (publication_id) REFERENCES publication(id)
);