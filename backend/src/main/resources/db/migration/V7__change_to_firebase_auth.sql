ALTER TABLE users
DROP COLUMN password_hash;

ALTER TABLE users
ADD COLUMN firebase_uid VARCHAR(128) NOT NULL;

ALTER TABLE users
ADD CONSTRAINT users_firebase_uid_unique UNIQUE (firebase_uid);