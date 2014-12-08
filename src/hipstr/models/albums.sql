-- name: get-recently-added
-- Gets the 10 most recently added albums in the db.
SELECT art.name as artist, alb.album_id, alb.name as album_name, alb.release_date, alb.created_at
FROM artists art
INNER JOIN albums alb ON art.artist_id = alb.artist_id
ORDER BY alb.created_at DESC
LIMIT 10;

-- name: get-by-artist
-- Gets the albums for a given artist.
-- Expects :artist, the artist name.
SELECT alb.album_id, alb.name AS album_name, alb.release_date, alb.created_at
FROM albums alb
INNER JOIN artists art ON alb.artist_id = art.artist_id
WHERE art.name = :artist
ORDER BY alb.release_date DESC;

-- name: insert-album<!
-- Adds the album for the given artist to the database
-- EXPECTS :artist_id, :album_name, and :release_date
INSERT INTO albums (artist_id, name, release_date)
VALUES (:artist_id, :album_name, date(:release_date));

-- name: get-albums-by-name
-- Fetches the specific album from the database for a particular artist.
-- Expects :artist_id and :album_name.
SELECT *
FROM albums
WHERE
  artist_id = :artist_id and
  name = :album_name;
