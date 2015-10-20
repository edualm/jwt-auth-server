# Users schema

# --- !Ups

CREATE TABLE demo (
    id      INT              NOT NULL,
    foo     TEXT             NOT NULL,
    PRIMARY KEY (id)
);

# --- !Downs

DROP TABLE demo;