-- name: insert-artist<!
-- Inserts a new artist into the database.
-- Expects :name.
INSERT INTO artists(name)
VALUES (:artist_name);

-- name: get-artists-by-name
-- Retrieves an artist from the database by name.
-- Expects :artist_name
SELECT *
FROM artists
WHERE name=:artist_name;
