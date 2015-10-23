# Groups, users, ...

# --- !Ups

CREATE TABLE category (
  id  SERIAL,
  name  VARCHAR(64) UNIQUE NOT NULL,
  type  VARCHAR(32) NOT NULL,
  PRIMARY KEY (id)
);

CREATE TABLE category_user (
  category_id INT REFERENCES category(id) NOT NULL,
  user_id INT REFERENCES user_data(id) NOT NULL,
  CONSTRAINT category_user_pkey PRIMARY KEY (category_id, user_id)
);

# --- !Downs

DROP TABLE category;
DROP TABLE category_user;