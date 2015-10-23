# Drop username from User Attributes

# --- !Ups

ALTER TABLE user_attribute DROP COLUMN username

# --- !Downs

ALTER TABLE user_attribute ADD COLUMN username VARCHAR(64) UNIQUE NOT NULL;