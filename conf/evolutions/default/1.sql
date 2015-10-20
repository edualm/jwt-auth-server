# JWT-Auth-Server schema

# --- !Ups

CREATE TABLE user_data (
  id  SERIAL,
  username  VARCHAR(64) NOT NULL,
  passwordDigest  VARCHAR(256) NOT NULL,
  passwordSalt VARCHAR(256) NOT NULL,
  emailAddress VARCHAR(256) NOT NULL,
  PRIMARY KEY (id)
);

# --- !Downs

DROP TABLE user_data;