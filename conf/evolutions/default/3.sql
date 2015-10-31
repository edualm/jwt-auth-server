# First Name / Last Name Fix

# --- !Ups

ALTER TABLE user_data ADD COLUMN first_name VARCHAR(64) NOT NULL;
ALTER TABLE user_data ADD COLUMN last_name VARCHAR(64) NOT NULL;

# --- !Downs

ALTER TABLE user_data DROP COLUMN first_name;
ALTER TABLE user_data DROP COLUMN last_name;
