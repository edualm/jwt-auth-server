# Users schema

# --- !Ups

CREATE TABLE Demo (
    id      INT              NOT NULL,
    foo     TEXT             NOT NULL,
    PRIMARY KEY (id)
);

# --- !Downs

DROP TABLE Demo;