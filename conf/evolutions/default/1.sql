# JWT-Auth-Server schema

# --- !Ups

CREATE TABLE user_data (
  id  SERIAL,
  username  VARCHAR(64) UNIQUE NOT NULL,
  password_digest  VARCHAR(256) NOT NULL,
  password_salt VARCHAR(256) NOT NULL,
  email_address VARCHAR(256) UNIQUE NOT NULL,
  PRIMARY KEY (id)
);

# --- !Downs

DROP TABLE user_data;