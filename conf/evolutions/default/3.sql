# Add enabled and signup time to user data

# --- !Ups

ALTER TABLE user_data ADD COLUMN enabled BOOLEAN NOT NULL default TRUE;
ALTER TABLE user_data ADD COLUMN signup_time TIMESTAMP NOT NULL default CURRENT_TIMESTAMP;

# --- !Downs

ALTER TABLE user_data DROP COLUMN enabled;
ALTER TABLE user_data DROP COLUMN signup_time;