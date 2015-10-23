# DB Structure

# --- !Ups

CREATE TABLE user_data (
  id  SERIAL,
  username  VARCHAR(64) UNIQUE NOT NULL,
  password_digest  VARCHAR(256) NOT NULL,
  password_salt VARCHAR(256) NOT NULL,
  email_address VARCHAR(256) UNIQUE NOT NULL,
  enabled BOOLEAN NOT NULL DEFAULT TRUE,
  signup_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (id)
);

CREATE TABLE user_attribute (
  id  SERIAL,
  user_id INT REFERENCES user_data(id) NOT NULL,
  key  VARCHAR(256) NOT NULL,
  value TEXT NOT NULL,
  PRIMARY KEY (id)
);

CREATE TABLE configuration (
  id  SERIAL,
  key  VARCHAR(256) NOT NULL,
  value TEXT NOT NULL,
  PRIMARY KEY (id)
);

CREATE TABLE category (
  id  SERIAL,
  name  VARCHAR(64) UNIQUE NOT NULL,
  type  VARCHAR(32) NOT NULL,
  PRIMARY KEY (id)
);

CREATE TABLE category_user (
  id  SERIAL,
  category_id INT REFERENCES category(id) NOT NULL,
  user_id INT REFERENCES user_data(id) NOT NULL,
  CONSTRAINT category_user_pkey PRIMARY KEY (category_id, user_id),
  PRIMARY KEY (id)
);

# --- !Downs

DROP TABLE user_data;
DROP TABLE user_attribute;
DROP TABLE configuration;
DROP TABLE category;
DROP TABLE category_user;