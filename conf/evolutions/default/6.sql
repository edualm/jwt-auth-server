# Drop username from User Attributes

# --- !Ups

ALTER TABLE user_data DROP COLUMN username

# --- !Downs

ALTER TABLE user_data ADD COLUMN username VARCHAR(64) UNIQUE NOT NULL;