# Configuration table

# --- !Ups

CREATE TABLE configuration (
  id  SERIAL,
  key  VARCHAR(256) NOT NULL,
  value TEXT NOT NULL,
  PRIMARY KEY (id)
);

# --- !Downs

DROP TABLE configuration;