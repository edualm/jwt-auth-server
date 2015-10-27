# ManyToMany Fix

# --- !Ups

ALTER TABLE category_user DROP COLUMN id;

# --- !Downs

ALTER TABLE column_user ADD COLUMN id SERIAL