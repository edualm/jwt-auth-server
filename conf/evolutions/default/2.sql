# User Attribute table

# --- !Ups

CREATE TABLE user_attribute (
  id  SERIAL,
  user_id INT REFERENCES user_data(id) NOT NULL,
  username  VARCHAR(64) UNIQUE NOT NULL,
  key  VARCHAR(256) NOT NULL,
  value TEXT NOT NULL,
  PRIMARY KEY (id)
);

# --- !Downs

DROP TABLE user_attribute;