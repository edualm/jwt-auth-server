# Groups, users, ...

# --- !Ups

CREATE TABLE group (
  id  SERIAL,
  name  VARCHAR(64) UNIQUE NOT NULL,
  type  VARCHAR(32) NOT NULL,
  PRIMARY KEY (id)
);

CREATE TABLE group_user (
  group_id INT REFERENCES group(id),
  user_id INT REFERENCES user_data(id),
  CONSTRAINT group_user_pkey PRIMARY KEY (group_id, user_id)
);

# --- !Downs

DROP TABLE group;
DROP TABLE group_user;